package com.zhufucdev.motion_emulator.ui

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.EditTextPreferenceDialogFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Material 3 风格的 EditTextPreference 对话框
 *
 * 使用 [MaterialAlertDialogBuilder] 替换默认的 EditTextPreference 对话框，
 * 以匹配 Material 3 的设计风格。
 */
class EditTextPreferenceM3DialogFragment : EditTextPreferenceDialogFragmentCompat() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(preference.dialogTitle)
            .setPositiveButton(preference.positiveButtonText, this)
            .setNegativeButton(preference.negativeButtonText, this)

        val view = context?.let { onCreateDialogView(it) }
        if (view == null) {
            builder.setMessage(preference.dialogMessage)
        } else {
            onBindDialogView(view)
            builder.setView(view)
        }
        return builder.create()
    }

    companion object {
        /**
         * 显示 EditTextPreference 对话框
         *
         * @param instance 要显示的 EditTextPreference 实例
         * @param parent 父 Fragment
         */
        fun show(instance: EditTextPreference, parent: Fragment) {
            val fragment = EditTextPreferenceM3DialogFragment()
            fragment.arguments = bundleOf("key" to instance.key)
            fragment.setTargetFragment(parent, 0)
            fragment.show(parent.parentFragmentManager, "androidx.preference.PreferenceFragment.DIALOG")
        }
    }
}
