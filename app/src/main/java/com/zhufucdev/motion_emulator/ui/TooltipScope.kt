package com.zhufucdev.motion_emulator.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface TooltipScope {
    fun Modifier.tooltip(content: @Composable () -> Unit): Modifier
}
