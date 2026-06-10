package com.zhufucdev.motion_emulator.ui

import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

/**
 * Material 3 风格的 PreferenceFragment 基类
 *
 * 重写 [onDisplayPreferenceDialog] 方法，使用 Material 3 风格的
 * 对话框来显示 [ListPreference] 和 [EditTextPreference] 的编辑界面。
 *
 * 所有设置页面的 Fragment 都应继承此类。
 */
abstract class M3PreferenceFragment : PreferenceFragmentCompat() {
    override fun onDisplayPreferenceDialog(preference: Preference) {
        when (preference) {
            is ListPreference -> {
                ListPreferenceM3DialogFragment.show(preference, this)
            }
            is EditTextPreference -> {
                EditTextPreferenceM3DialogFragment.show(preference, this)
            }
            else -> super.onDisplayPreferenceDialog(preference)
        }
    }
}
