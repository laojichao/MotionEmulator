package com.zhufucdev.motion_emulator.ui

import android.content.DialogInterface
import android.database.MatrixCursor
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.zhufucdev.motion_emulator.R
import com.zhufucdev.motion_emulator.data.DataStore
import com.zhufucdev.motion_emulator.data.Traces
import com.zhufucdev.motion_emulator.databinding.ActivityTraceDrawingBinding
import com.zhufucdev.motion_emulator.extension.initializeToolbar
import com.zhufucdev.motion_emulator.extension.lazySharedPreferences
import com.zhufucdev.motion_emulator.extension.skipAmapFuckingLicense
import com.zhufucdev.motion_emulator.extension.toPoint
import com.zhufucdev.motion_emulator.ui.map.AMapPoiEngine
import com.zhufucdev.motion_emulator.ui.map.GooglePoiEngine
import com.zhufucdev.motion_emulator.ui.map.MapStyle
import com.zhufucdev.motion_emulator.ui.map.PoiSearchEngine
import com.zhufucdev.motion_emulator.ui.map.UnifiedMapFragment.Provider
import com.zhufucdev.me.stub.Point
import com.zhufucdev.me.stub.Trace
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

class TraceDrawingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTraceDrawingBinding
    private lateinit var locationManager: LocationManager
    private val preferences by lazySharedPreferences()
    private val traces = ArrayList<Trace>()
    private var currentTool: ToolCallback<*> = MoveToolCallback

    private val poiEngine: PoiSearchEngine by lazy {
        when (getProvider("poi_provider")) {
            Provider.AMAP -> AMapPoiEngine(this)
            Provider.GCP_MAPS -> GooglePoiEngine(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        skipAmapFuckingLicense(this)
        binding = ActivityTraceDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeToolbar(this, binding.appBarToolbar)
        locationManager = getSystemService(LocationManager::class.java)
        Traces.require(this)

        binding.mapUnified.provider = getProvider("map_provider")

        if (Build.VERSION.SDK_INT >= 29) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        } else {
            involveLocation()
        }

        initializeToolSlots()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.trace_drawing_actionbar, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        initializeSearch(searchView)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val controller = binding.mapUnified.controller ?: return false
        val style = when (item.itemId) {
            R.id.app_bar_type_common -> MapStyle.NORMAL
            R.id.app_bar_type_night -> MapStyle.NIGHT
            R.id.app_bar_type_satellite -> MapStyle.SATELLITE
            else -> return false
        }
        controller.displayStyle = style
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.first() == 0) {
            involveLocation()
        }
    }

    override fun finish() {
        if (traces.isEmpty() && currentTool !is DrawToolCallback && currentTool !is GpsToolCallback) {
            super.finish()
            return
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.title_unsaved_trace)
            .setMessage(R.string.text_unsaved_trace)
            .setNegativeButton(R.string.action_cancel, null)
            .setPositiveButton(R.string.action_continue) { _, _ -> super.finish() }
            .show()
    }

    // ---- Provider ----
    private fun getProvider(key: String): Provider {
        val value = preferences.getString(key, "gcp_maps")!!
        return Provider.valueOf(value.uppercase(Locale.ROOT))
    }

    // ---- Search ----
    private fun initializeSearch(searchView: SearchView) {
        val adapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            null,
            arrayOf("name", "city"),
            intArrayOf(android.R.id.text1, android.R.id.text2),
            2
        )
        searchView.suggestionsAdapter = adapter
        val handler = Handler(mainLooper)
        var lastQuery: String? = null
        var lastResults: List<com.zhufucdev.motion_emulator.ui.map.Poi>? = null

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                lastQuery = newText
                handler.postDelayed({
                    if (lastQuery == newText && newText.isNotEmpty()) {
                        lifecycleScope.launch {
                            lastResults = poiEngine.search(newText, 20)
                            val cursor = MatrixCursor(arrayOf("_id", "name", "city"))
                            lastResults!!.forEachIndexed { index, poi ->
                                val city = if (poi.province.isNotEmpty()) {
                                    getString(R.string.name_location, poi.province, poi.city)
                                } else {
                                    poi.city
                                }
                                cursor.addRow(arrayOf(index, poi.name, city))
                            }
                            adapter.changeCursor(cursor)
                        }
                    }
                }, 1000)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean = false
        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionClick(position: Int): Boolean {
                val results = lastResults ?: return false
                val poi = results[position]
                val point = poi.location ?: return false
                searchView.setQuery("", false)
                searchView.isIconified = true
                lifecycleScope.launch {
                    val controller = binding.mapUnified.requireController()
                    controller.moveCamera(point, true, true)
                }
                return true
            }

            override fun onSuggestionSelect(position: Int): Boolean = false
        })
    }

    // ---- Tool Slots ----
    private fun initializeToolSlots() {
        val menu = binding.toolSlots.menu

        binding.toolSlots.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tool_undo -> {
                    currentTool.undo()
                    false
                }
                R.id.slot_hand -> {
                    currentTool = useMove()
                    true
                }
                R.id.slot_add -> {
                    binding.toolSlots.menu.clear()
                    menuInflater.inflate(R.menu.trace_drawing_tool_selection, binding.toolSlots.menu)
                    resetMenu(menu)
                    binding.toolSlots.selectedItemId = R.id.tool_draw
                    false
                }
                R.id.slot_save -> {
                    lifecycleScope.launch {
                        currentTool.complete()
                        if (traces.isNotEmpty()) {
                            traces.forEach { Traces.store(it) }
                            runOnUiThread {
                                Snackbar.make(
                                    binding.root,
                                    getString(R.string.text_trace_saved, traces.size),
                                    Snackbar.LENGTH_SHORT
                                ).setAnchorView(binding.toolSlots).show()
                            }
                            traces.clear()
                        }
                    }
                    false
                }
                R.id.tool_draw, R.id.tool_gps -> true
                R.id.tool_done -> {
                    lifecycleScope.launch { currentTool.complete() }
                    menu.clear()
                    menuInflater.inflate(R.menu.trace_drawing_tool_slots, menu)
                    binding.toolSlots.selectedItemId = R.id.slot_hand
                    resetMenu(menu)
                    false
                }
                R.id.tool_clear -> {
                    val tool = currentTool
                    if (tool is DrawToolCallback) {
                        tool.clear()
                    }
                    false
                }
                R.id.tool_pause -> {
                    val tool = currentTool
                    if (tool is GpsToolCallback) {
                        if (tool.isPaused()) {
                            tool.unpause()
                            item.setIcon(R.drawable.ic_baseline_pause_24)
                            item.setTitle(R.string.action_pause)
                        } else {
                            tool.pause()
                            item.setIcon(R.drawable.ic_baseline_fiber_manual_record_24)
                            item.setTitle(R.string.action_unpause)
                        }
                    }
                    false
                }
                R.id.tool_opt -> {
                    when (binding.toolSlots.selectedItemId) {
                        R.id.tool_draw -> {
                            menu.clear()
                            menuInflater.inflate(R.menu.trace_drawing_tool_draw, menu)
                            resetMenu(menu)
                            binding.toolSlots.selectedItemId = R.id.tool_draw
                            runBlocking {
                                currentTool = useDraw()
                            }
                            false
                        }
                        R.id.tool_gps -> {
                            menu.clear()
                            menuInflater.inflate(R.menu.trace_drawing_tool_gps, menu)
                            resetMenu(menu)
                            binding.toolSlots.selectedItemId = R.id.tool_gps
                            lifecycleScope.launch {
                                try {
                                    currentTool = useGps()
                                } catch (e: RuntimeException) {
                                    MaterialAlertDialogBuilder(this@TraceDrawingActivity)
                                        .setTitle(R.string.title_no_gps_provider)
                                        .setMessage(R.string.text_no_gps_provider)
                                        .setNegativeButton(R.string.action_cancel, null)
                                        .show()
                                }
                            }
                            false
                        }
                        else -> {
                            throw NotImplementedError("Unknown tool: ${menu.findItem(binding.toolSlots.selectedItemId).title}")
                        }
                    }
                }
                else -> false
            }
        }
    }

    private fun resetMenu(menu: Menu) {
        val lastItem = menu.getItem(menu.size() - 1)
        lastItem.isVisible = false
        lastItem.isVisible = true
    }

    // ---- Tools ----
    private suspend fun useDraw(): DrawToolCallback {
        binding.mapUnified.isFocusable = false
        val controller = binding.mapUnified.requireController()
        val drawTool = controller.useDraw(binding.touchReceiver)
        addCompleteListener(drawTool)
        return drawTool
    }

    private suspend fun useGps(): GpsToolCallback {
        if (Build.VERSION.SDK_INT >= 29) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
        val snackbar = Snackbar.make(binding.root, R.string.text_gps_pending, Snackbar.LENGTH_INDEFINITE)
            .setAnchorView(binding.toolSlots)
        snackbar.show()
        val controller = binding.mapUnified.requireController()
        val gpsTool = controller.useGps()
        snackbar.dismiss()
        addCompleteListener(gpsTool)
        return gpsTool
    }

    private fun useMove(): MoveToolCallback = MoveToolCallback

    private fun addCompleteListener(tool: ToolCallback<*>) {
        tool.onCompleted { result ->
            val drawResult = result as DrawResult
            val id = NanoIdUtils.randomNanoId()
            traces.add(Trace(id, "", drawResult.trace, drawResult.coordinateSystem))
            runOnUiThread {
                Snackbar.make(
                    binding.root,
                    getString(R.string.text_trace_name, ""),
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(binding.toolSlots).show()
            }
        }
    }

    // ---- Location ----
    private fun involveLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != 0 &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != 0
        ) return

        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_COARSE
            isSpeedRequired = false
        }
        val provider = locationManager.getBestProvider(criteria, true) ?: return
        val lastLocation = locationManager.getLastKnownLocation(provider)

        if (lastLocation != null) {
            lifecycleScope.launch { notifyLocated(lastLocation) }
            return
        }

        if (Build.VERSION.SDK_INT >= 30) {
            locationManager.getCurrentLocation(provider, null, mainExecutor) { location ->
                if (location != null) {
                    lifecycleScope.launch { notifyLocated(location) }
                } else {
                    Log.w("TraceDrawing", "failed to obtain location")
                }
            }
            return
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                lifecycleScope.launch { notifyLocated(location) }
                locationManager.removeUpdates(this)
            }
        }
        locationManager.requestLocationUpdates(provider, 50000L, 0f, listener)
    }

    private suspend fun notifyLocated(location: Location) {
        val controller = binding.mapUnified.requireController()
        controller.updateLocationIndicator(location)
        controller.moveCamera(location.toPoint(), true, false)
    }
}
