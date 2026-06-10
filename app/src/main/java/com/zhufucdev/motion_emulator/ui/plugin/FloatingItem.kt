package com.zhufucdev.motion_emulator.ui.plugin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

class FloatingItem(
    val plugin: PluginItem,
    val offset: Offset,
    position: Offset
) {
    var position by mutableStateOf(position)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as FloatingItem
        return plugin == other.plugin && offset == other.offset
    }

    override fun hashCode(): Int {
        return plugin.hashCode() * 31 + offset.hashCode()
    }
}
