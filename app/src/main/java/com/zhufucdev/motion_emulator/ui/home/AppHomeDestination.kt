package com.zhufucdev.motion_emulator.ui.home

import com.zhufucdev.motion_emulator.ui.EmulateActivity
import com.zhufucdev.motion_emulator.ui.ManagerActivity
import com.zhufucdev.motion_emulator.ui.PluginActivity
import com.zhufucdev.motion_emulator.ui.RecordActivity
import com.zhufucdev.motion_emulator.ui.SettingsActivity
import com.zhufucdev.motion_emulator.ui.TraceDrawingActivity

enum class AppHomeDestination(
    val activity: Class<*>,
    val mapping: Boolean = false
) {
    Plugins(PluginActivity::class.java),
    Record(RecordActivity::class.java),
    Trace(TraceDrawingActivity::class.java, mapping = true),
    Emulation(EmulateActivity::class.java, mapping = true),
    Management(ManagerActivity::class.java),
    Settings(SettingsActivity::class.java);
}
