package com.zhufucdev.motion_emulator.ui.emulate

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zhufucdev.motion_emulator.R
import com.zhufucdev.motion_emulator.data.AppMeta
import com.zhufucdev.motion_emulator.databinding.FragmentEmulationAppBinding
import com.zhufucdev.motion_emulator.extension.toFixed
import com.zhufucdev.motion_emulator.provider.Scheduler
import com.zhufucdev.motion_emulator.ui.map.MapController
import com.zhufucdev.me.stub.AgentState
import com.zhufucdev.me.stub.EmulationInfo
import com.zhufucdev.me.stub.android
import kotlin.NotImplementedError
import kotlin.math.roundToInt

class EmulationAppFragment : EmulationMonitoringFragment() {
    private lateinit var binding: FragmentEmulationAppBinding
    private lateinit var id: String
    private lateinit var packageManager: PackageManager
    var map: MapController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        packageManager = requireContext().packageManager
        binding = FragmentEmulationAppBinding.inflate(layoutInflater, container, false)
        id = arguments?.getString("target_id")!!

        val wrapper = FrameLayout(requireContext())
        wrapper.addView(binding.root)
        val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 12f, requireContext().resources.displayMetrics
        ).roundToInt()
        wrapper.setPadding(padding, padding, padding, padding)
        return wrapper
    }

    override fun onResume() {
        super.onResume()
        notifyState(Scheduler.currentEmulationState(id))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addIntermediateListener { agentId, info ->
            if (agentId != id) return@addIntermediateListener
            requireActivity().runOnUiThread {
                notifyProgress(info.progress)
                val emulationInfo = Scheduler.instance[id]
                if (emulationInfo != null) {
                    notifyTime(emulationInfo.duration - info.elapsed)
                }
                map?.updateLocationIndicator(info.location.android())
            }
        }
        notifyState(AgentState.PENDING)
        addEmulationStateListener { agentId, state ->
            if (agentId != id) return@addEmulationStateListener
            requireActivity().runOnUiThread { notifyState(state) }
        }
    }

    private fun notifyState(state: AgentState) {
        val info: EmulationInfo? = Scheduler.instance[id]
        when (state) {
            AgentState.NOT_JOINED -> notifyOffline()
            AgentState.PENDING -> notifyPending()
            AgentState.RUNNING -> {
                info!!
                notifyStarted(info)
            }
            AgentState.CANCELED -> notifyStopped(R.string.title_emulation_canceled)
            AgentState.PAUSED -> throw NotImplementedError()
            AgentState.COMPLETED -> notifyStopped(R.string.title_emulation_completed)
            AgentState.FAILURE -> notifyStopped(R.string.title_emulation_failure)
        }
    }

    private fun notifyOffline() {
        binding.titleEmulationStatus.setText(R.string.title_agent_offline)
        binding.textEmulationStatus.setText(R.string.text_emulation_pending)
        binding.textEmulationStatus.visibility = View.VISIBLE
        binding.stackMonitors.root.visibility = View.GONE
        binding.stackAppReceived.root.visibility = View.GONE
        binding.btnDetermine.visibility = View.GONE
        binding.progressEmulation.visibility = View.VISIBLE
    }

    private fun notifyPending() {
        binding.titleEmulationStatus.setText(R.string.title_emulation_pending)
        binding.textEmulationStatus.setText(R.string.text_emulation_app_pending)
        binding.textEmulationStatus.visibility = View.VISIBLE
        binding.stackMonitors.root.visibility = View.GONE
        binding.stackAppReceived.root.visibility = View.GONE
        binding.btnDetermine.visibility = View.GONE
        binding.progressEmulation.isIndeterminate = true
        binding.progressEmulation.visibility = View.VISIBLE
    }

    private fun notifyStarted(info: EmulationInfo) {
        val context = requireContext()
        binding.titleEmulationStatus.text =
            getString(R.string.title_named_emulation_ongoing, id.substring(0..4))

        val appInfo = packageManager.getApplicationInfo(info.owner, 0x80)
        val appMeta = AppMeta.of(appInfo, packageManager)
        binding.stackAppReceived.textAppPicked.text = getString(R.string.text_app_received, appMeta.name)
        binding.stackAppReceived.iconView.setImageDrawable(appMeta.icon)
        binding.stackAppReceived.root.visibility = View.VISIBLE

        val emulation = Scheduler.emulation!!
        binding.stackMonitors.textStatusVelocity.text =
            getString(R.string.status_velocity, "${emulation.velocity} m/s")
        binding.stackMonitors.textStatusLength.text =
            getString(R.string.status_total, "${info.length.toFixed(2)}m")
        notifyTime(info.duration)
        binding.stackMonitors.root.visibility = View.VISIBLE
        binding.textEmulationStatus.visibility = View.GONE

        binding.btnDetermine.setOnClickListener { Scheduler.cancelAgent(id) }
        binding.btnDetermine.setText(R.string.action_determine)
        binding.btnDetermine.visibility = View.VISIBLE

        binding.progressEmulation.visibility = View.VISIBLE
        binding.progressEmulation.isIndeterminate = false

        val intermediate = Scheduler.intermediate[id]
        if (intermediate != null) {
            notifyProgress(intermediate.progress)
            map?.updateLocationIndicator(intermediate.location.android())
            map?.moveCamera(intermediate.location, true, true)
        }
    }

    private fun notifyStopped(titleRes: Int) {
        binding.titleEmulationStatus.setText(titleRes)
        binding.textEmulationStatus.visibility = View.VISIBLE
        binding.stackMonitors.root.visibility = View.GONE
        binding.stackAppReceived.root.visibility = View.GONE
        binding.btnDetermine.setOnClickListener {
            Scheduler.startAgent(id)
            notifyPending()
        }
        binding.btnDetermine.setText(R.string.action_restart)
        binding.btnDetermine.visibility = View.VISIBLE
        binding.progressEmulation.visibility = View.GONE
    }

    private fun notifyProgress(progress: Float) {
        binding.progressEmulation.setProgress(
            (binding.progressEmulation.max * progress).roundToInt(), true
        )
    }

    private fun notifyTime(remaining: Double) {
        val s = remaining.toFixed(2)
        binding.stackMonitors.textStatusTime.text = getString(R.string.status_remaining, "${s}s")
    }
}
