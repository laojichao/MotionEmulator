package com.zhufucdev.motion_emulator.ui.plugin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class PluginItem(
    val id: String,
    val title: String,
    val subtitle: String = "",
    enabled: Boolean,
    state: PluginItemState
) {
    var enabled by mutableStateOf(enabled)
    var state by mutableStateOf(state)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as PluginItem
        return id == other.id && enabled == other.enabled && state == other.state
    }

    override fun hashCode(): Int {
        return (id.hashCode() * 31 + enabled.hashCode()) * 31 + state.hashCode()
    }
}
