package com.zhufucdev.motion_emulator.ui.emulate

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zhufucdev.motion_emulator.R
import com.zhufucdev.motion_emulator.data.Cells
import com.zhufucdev.motion_emulator.data.Motions
import com.zhufucdev.motion_emulator.data.Traces
import com.zhufucdev.motion_emulator.databinding.FragmentConfigurationBinding
import com.zhufucdev.motion_emulator.extension.effectiveTimeFormat
import com.zhufucdev.motion_emulator.extension.lazySharedPreferences
import com.zhufucdev.motion_emulator.extension.ref
import com.zhufucdev.motion_emulator.extension.skipAmapFuckingLicense
import com.zhufucdev.motion_emulator.provider.EmulationRef
import com.zhufucdev.motion_emulator.provider.Scheduler
import com.zhufucdev.motion_emulator.ui.map.MapController
import com.zhufucdev.motion_emulator.ui.map.MapDisplayType
import com.zhufucdev.motion_emulator.ui.map.MapTraceCallback
import com.zhufucdev.motion_emulator.ui.map.TraceBounds
import com.zhufucdev.motion_emulator.ui.map.UnifiedMapFragment.Provider
import com.zhufucdev.me.stub.BlockBox
import com.zhufucdev.me.stub.Box
import com.zhufucdev.motion_emulator.extension.box
import com.zhufucdev.me.stub.CellTimeline
import com.zhufucdev.me.stub.EmptyBox
import com.zhufucdev.me.stub.Emulation
import com.zhufucdev.me.stub.Motion
import com.zhufucdev.me.stub.Trace
import kotlinx.coroutines.launch
import net.edwardday.serialization.preferences.Preferences
import java.text.DateFormat
import java.util.Locale

class ConfigurationFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentConfigurationBinding
    private var btnDefault: MenuItem? = null
    private lateinit var btnRun: ExtendedFloatingActionButton

    private var trace: Trace? = null
    private var motion: Box<Motion> = EmptyBox()
    private var cells: Box<CellTimeline> = EmptyBox()
    private var repeatCount: Int? = 1
    private var velocity: Double? = 3.0
    private var satelliteCount: Int? = 10
    private var drawnTrace: MapTraceCallback? = null

    private val settings by lazy {
        requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
    private val defaultConfig by lazy {
        try {
            Preferences(settings).decode(EmulationRef.serializer(), "default_config")
        } catch (e: Exception) {
            null
        }
    }
    private val defaultPreferences by lazySharedPreferences()
    private val dateFormat: DateFormat by lazy {
        defaultPreferences.effectiveTimeFormat()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        skipAmapFuckingLicense(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigurationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val provider = defaultPreferences.getString("map_provider", "gcp_maps")!!
        binding.mapTracePreview.provider = Provider.valueOf(provider.uppercase(Locale.ROOT))

        btnRun = binding.btnRunEmulation

        lifecycleScope.launch {
            initTracesDropdown()
            val controller = binding.mapTracePreview.requireController()
            controller.displayType = MapDisplayType.STILL
        }

        initMotionDropdown()
        initCellsDropdown()
        initializeOthers()
        notifyContinue()
    }

    override fun onResume() {
        super.onResume()
        notifyContinue()
    }

    // MenuProvider
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.config_actionbar, menu)
        btnDefault = menu.findItem(R.id.app_bar_default)
        btnDefault?.isEnabled = !notContinue
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.app_bar_default) {
            val emulation = emulation() ?: return false
            val emulationRef = ref(emulation)
            Preferences(settings).encode(EmulationRef.serializer(), "default_config", emulationRef)
            Snackbar.make(btnRun, R.string.text_saved_as_default, Snackbar.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    // Dropdowns
    private suspend fun initTracesDropdown() {
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line)
        val traces = Traces.list()
        traces.forEach { adapter.add(it.value.name ?: it.value.id) }

        val dropdown = binding.dropdownTrace
        dropdown.setOnItemClickListener { _, _, position, _ ->
            lifecycleScope.launch {
                selectTrace(traces[position].value)
            }
        }
        dropdown.setAdapter(adapter)

        defaultConfig?.trace?.let { id ->
            val trace = traces.find { it.id == id }
            if (trace != null) {
                select(dropdown, adapter, trace.value.name ?: trace.value.id)
                selectTrace(trace.value)
            }
        }
    }

    private fun initMotionDropdown() {
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line)
        val motions = Motions.list()
        motions.forEach { adapter.add(it.value.getDisplayName(dateFormat)) }
        addDefaults(adapter)

        val dropdown = binding.dropdownMotion
        dropdown.setOnItemClickListener { _, _, position, _ ->
            this.motion = if (position < motions.size) {
                box(motions[position].value)
            } else if (position == motions.size) {
                EmptyBox()
            } else {
                BlockBox()
            }
            notifyContinue()
        }
        dropdown.setAdapter(adapter)

        val id = defaultConfig?.motion
        if (id == null) {
            selectDefaults(dropdown, adapter, "none")
        } else {
            if (!selectDefaults(dropdown, adapter, id)) {
                motions.find { it.id == id }?.let {
                    select(dropdown, adapter, it.value.getDisplayName(dateFormat))
                    this.motion = box(it.value)
                }
            }
        }
    }

    private fun initCellsDropdown() {
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line)
        val timelines = Cells.list()
        timelines.forEach { adapter.add(it.value.getDisplayName(dateFormat)) }
        addDefaults(adapter)

        val dropdown = binding.dropdownCells
        dropdown.setOnItemClickListener { _, _, position, _ ->
            this.cells = if (position < timelines.size) {
                box(timelines[position].value)
            } else if (position == timelines.size) {
                EmptyBox()
            } else {
                BlockBox()
            }
            notifyContinue()
        }
        dropdown.setAdapter(adapter)

        val id = defaultConfig?.cells
        if (id == null) {
            selectDefaults(dropdown, adapter, "none")
        } else {
            if (!selectDefaults(dropdown, adapter, id)) {
                timelines.find { it.id == id }?.let {
                    select(dropdown, adapter, it.value.getDisplayName(dateFormat))
                    this.cells = box(it.value)
                }
            }
        }
    }

    private fun initializeOthers() {
        defaultConfig?.let { config ->
            binding.inputVelocity.setText(config.velocity.toString())
            this.velocity = config.velocity
            binding.inputRepeatCount.setText(config.repeat.toString())
            this.repeatCount = config.repeat
            binding.inputSatellite.setText(config.satelliteCount.toString())
            this.satelliteCount = config.satelliteCount
            notifyContinue()
        }

        binding.inputVelocity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                this@ConfigurationFragment.velocity = if (s.isNullOrEmpty()) null
                else s.toString().toDoubleOrNull()
                binding.inputVelocity.error = when {
                    this@ConfigurationFragment.velocity == null -> getString(R.string.text_field_must_not_empty)
                    this@ConfigurationFragment.velocity!! <= 0 -> getString(R.string.text_field_must_not_neg_or_zero)
                    else -> null
                }
                notifyContinue()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.inputRepeatCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                this@ConfigurationFragment.repeatCount = if (s.isNullOrEmpty()) null
                else s.toString().toIntOrNull()
                setError(binding.inputRepeatCount, this@ConfigurationFragment.repeatCount, true)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.inputSatellite.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                this@ConfigurationFragment.satelliteCount = if (s.isNullOrEmpty()) null
                else s.toString().toIntOrNull()
                setError(binding.inputSatellite, this@ConfigurationFragment.satelliteCount, false)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnRun.setOnClickListener {
            disable()
            startEmulation()
        }
    }

    private fun setError(input: TextInputEditText, value: Int?, mustBePositive: Boolean) {
        input.error = when {
            value == null -> getString(R.string.text_field_must_not_empty)
            mustBePositive && value <= 0 -> getString(R.string.text_field_must_not_neg_or_zero)
            else -> null
        }
        notifyContinue()
    }

    // Helpers
    private fun addDefaults(adapter: ArrayAdapter<String>) {
        adapter.add(getString(R.string.name_none))
        adapter.add(getString(R.string.name_block))
    }

    private fun select(dropdown: AutoCompleteTextView, adapter: ArrayAdapter<String>, name: String) {
        dropdown.setText(name)
        adapter.filter.filter(null)
    }

    private fun selectDefaults(dropdown: AutoCompleteTextView, adapter: ArrayAdapter<String>, id: String): Boolean {
        return when (id) {
            "none" -> {
                select(dropdown, adapter, getString(R.string.name_none))
                true
            }
            "block" -> {
                select(dropdown, adapter, getString(R.string.name_block))
                true
            }
            else -> false
        }
    }

    private suspend fun selectTrace(t: Trace) {
        trace = t
        notifyContinue()
        val controller = binding.mapTracePreview.requireController()
        drawnTrace?.remove()
        drawnTrace = controller.drawTrace(t)
        controller.boundCamera(TraceBounds(t), true)
    }

    private val notContinue: Boolean
        get() {
            val binding = this.binding
            return !binding.inputVelocity.error.isNullOrEmpty() ||
                    !binding.inputRepeatCount.error.isNullOrEmpty() ||
                    !binding.inputSatellite.error.isNullOrEmpty() ||
                    trace == null
        }

    private fun notifyContinue() {
        if (notContinue) {
            btnRun.hide()
            btnDefault?.isEnabled = false
        } else {
            btnRun.show()
            btnDefault?.isEnabled = true
        }
    }

    private fun disable() {
        btnRun.hide()
        inputWrappers.forEach { it.isEnabled = false }
    }

    private val inputWrappers: List<TextInputLayout>
        get() = listOf(
            binding.wrapperDropdown,
            binding.wrapperVelocity,
            binding.wrapperRepeatCount,
            binding.wrapperCellsDropdown,
            binding.wrapperSatellite
        )

    private fun emulation(): Emulation? {
        val r = this.repeatCount ?: return null
        if (r <= 0) return null
        val v = this.velocity ?: return null
        if (v <= 0.0) return null
        val s = this.satelliteCount ?: return null
        if (s < 0) return null
        val t = this.trace ?: return null
        return Emulation(t, this.motion, this.cells, v, r, s)
    }

    private fun startEmulation() {
        val emulation = emulation() ?: return
        Scheduler.emulation = emulation
        val t = this.trace!!
        findNavController().navigate(
            R.id.action_configurationFragment_to_emulateStatusFragment,
            bundleOf("target_trace" to t.id)
        )
        btnRun.hide()
    }
}
