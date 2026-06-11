package com.zhufucdev.motion_emulator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.zhufucdev.motion_emulator.extension.setUpStatusBar
import com.zhufucdev.motion_emulator.plugin.Plugins
import com.zhufucdev.motion_emulator.ui.model.PluginViewModel
import com.zhufucdev.motion_emulator.ui.model.toPluginItem
import com.zhufucdev.motion_emulator.ui.theme.MotionEmulatorTheme
import kotlinx.coroutines.flow.flow

class PluginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpStatusBar(this)
        setContent {
            val viewModel = remember {
                val enabled = Plugins.enabled
                val all = Plugins.available
                val plugins = enabled.map { it.toPluginItem(true) } +
                    (all - enabled.toSet()).map { it.toPluginItem(false) }
                PluginViewModel(plugins = plugins, downloadable = flow { })
            }
            MotionEmulatorTheme {
                PluginsApp(
                    paddingValues = PaddingValues(0.dp),
                    viewModel = viewModel
                )
            }
        }
    }
}
