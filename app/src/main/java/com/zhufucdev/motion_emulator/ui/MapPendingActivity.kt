package com.zhufucdev.motion_emulator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.zhufucdev.motion_emulator.ui.map.PendingApp
import com.zhufucdev.motion_emulator.ui.theme.MotionEmulatorTheme

class MapPendingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val target = intent.getStringExtra("target")
        val targetClass = Class.forName(target!!)
        setContent {
            MotionEmulatorTheme {
                PendingApp(targetClass) {
                    finish()
                }
            }
        }
    }
}
