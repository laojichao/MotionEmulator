package com.zhufucdev.motion_emulator.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BottomSheetModalState {
    var open: Boolean by mutableStateOf(false)
    var drawerContent: (@Composable () -> Unit)? by mutableStateOf(null)

    fun drawer(drawerContent: @Composable () -> Unit) {
        this.drawerContent = drawerContent
    }
}
