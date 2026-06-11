package com.zhufucdev.motion_emulator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import com.zhufucdev.motion_emulator.data.Cells
import com.zhufucdev.motion_emulator.data.Motions
import com.zhufucdev.motion_emulator.data.Traces
import com.zhufucdev.motion_emulator.extension.setUpStatusBar
import com.zhufucdev.motion_emulator.ui.manager.*
import com.zhufucdev.motion_emulator.ui.theme.MotionEmulatorTheme

class ManagerActivity : ComponentActivity() {
    private val viewModels by lazy {
        val provider = androidx.lifecycle.ViewModelProvider(this)
        listOf(
            provider.get(ManagerViewModel.OverviewViewModel::class.java),
            provider.get(EditorViewModel.MotionViewModel::class.java),
            provider.get(EditorViewModel.CellsViewModel::class.java),
            provider.get(EditorViewModel.TraceViewModel::class.java)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Motions.require(this)
        Cells.require(this)
        Traces.require(this)
        setUpStatusBar(this)
        setContent {
            MotionEmulatorTheme {
                CompositionLocalProvider(
                    LocalScreenProviders provides ScreenProviders(viewModels)
                ) {
                    ManagerApp(paddingValues = PaddingValues(0.dp))
                }
            }
        }
    }
}
