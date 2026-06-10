package com.zhufucdev.motion_emulator.ui.emulate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.zhufucdev.motion_emulator.data.Traces
import com.zhufucdev.motion_emulator.databinding.FragmentEmulateStatusBinding
import com.zhufucdev.motion_emulator.extension.skipAmapFuckingLicense
import com.zhufucdev.motion_emulator.extension.lazySharedPreferences
import com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker
import com.zhufucdev.motion_emulator.provider.Scheduler
import com.zhufucdev.motion_emulator.ui.map.MapTraceCallback
import com.zhufucdev.motion_emulator.ui.map.TraceBounds
import com.zhufucdev.motion_emulator.ui.map.UnifiedMapFragment.Provider
import kotlinx.coroutines.launch
import java.util.Locale

class EmulateStatusFragment : Fragment() {
    private lateinit var binding: FragmentEmulateStatusBinding
    private val preferences by lazySharedPreferences()
    private var previousTrace: MapTraceCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Scheduler.init(requireContext())
        skipAmapFuckingLicense(requireContext())
        registerChannel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmulateStatusBinding.inflate(layoutInflater, container, false)
        val provider = preferences.getString("map_provider", "gcp_maps")!!
        binding.mapMotionPreview.provider = Provider.valueOf(provider.uppercase(Locale.ROOT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initializeMonitors()
        }
    }

    override fun onResume() {
        super.onResume()
        removeMonitorWorker()
        lifecycleScope.launch {
            initializeMap()
        }
    }

    override fun onStop() {
        super.onStop()
        addMonitorWorker()
    }

    override fun onDestroy() {
        super.onDestroy()
        Scheduler.stop(requireContext())
        removeMonitorWorker()
    }

    private suspend fun initializeMap() {
        val controller = binding.mapMotionPreview.requireController()
        val targetTrace = arguments?.getString("target_trace")
        if (targetTrace != null) {
            val trace = Traces[targetTrace]?.value
            if (trace != null) {
                previousTrace?.remove()
                previousTrace = controller.drawTrace(trace)
                controller.boundCamera(TraceBounds(trace), false)
            }
        }
    }

    private fun initializeMonitors() {
        binding.viewpagerStatus.adapter =
            EmulationCardAdapter(this, binding.mapMotionPreview)
    }

    private fun registerChannel() {
        if (Build.VERSION.SDK_INT < 26) return
        if (Build.VERSION.SDK_INT >= 33) {
            requireActivity().requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0
            )
        }
        val context = requireContext().applicationContext
        val name = getString(com.zhufucdev.motion_emulator.R.string.title_channel_emulation)
        val desc = getString(com.zhufucdev.motion_emulator.R.string.text_channel_emulation)
        val channel = NotificationChannel("emulation_activity", name, NotificationManager.IMPORTANCE_LOW)
        channel.description = desc
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun addMonitorWorker() {
        val request = OneTimeWorkRequest.Builder(EmulationMonitorWorker::class.java).build()
        WorkManager.getInstance(requireContext())
            .enqueueUniqueWork("emulationMonitor", ExistingWorkPolicy.REPLACE, request)
    }

    private fun removeMonitorWorker() {
        WorkManager.getInstance(requireContext())
            .cancelUniqueWork("emulationMonitor")
    }
}
