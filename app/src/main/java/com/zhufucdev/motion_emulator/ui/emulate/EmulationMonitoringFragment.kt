package com.zhufucdev.motion_emulator.ui.emulate

import androidx.fragment.app.Fragment
import com.zhufucdev.motion_emulator.provider.ListenCallback
import com.zhufucdev.motion_emulator.provider.Scheduler
import com.zhufucdev.me.stub.AgentState
import com.zhufucdev.me.stub.Intermediate

abstract class EmulationMonitoringFragment : Fragment() {
    private val listeners = LinkedHashSet<ListenCallback>()

    protected fun addEmulationStateListener(l: (String, AgentState) -> Unit) {
        val callback = Scheduler.onAgentStateChanged(l)
        listeners.add(callback)
    }

    protected fun addIntermediateListener(l: (String, Intermediate) -> Unit) {
        val callback = Scheduler.addIntermediateListener(l)
        listeners.add(callback)
    }

    override fun onPause() {
        super.onPause()
        listeners.forEach {
            try { it.pause() } catch (_: IllegalStateException) {}
        }
    }

    override fun onResume() {
        super.onResume()
        listeners.forEach {
            try { it.resume() } catch (_: IllegalStateException) {}
        }
    }
}
