package com.zhufucdev.motion_emulator.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset

interface TooltipState {
    val isVisible: Boolean
    fun setVisible(value: Boolean)
    fun dismiss()
    suspend fun show(position: Offset, content: @Composable () -> Unit)
}
