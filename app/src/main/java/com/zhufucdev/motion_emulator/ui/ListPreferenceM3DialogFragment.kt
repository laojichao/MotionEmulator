package com.zhufucdev.motion_emulator.ui

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.preference.ListPreference
import androidx.preference.ListPreferenceDialogFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Material 3 风格的 ListPreference 对话框
 *
 * 使用 [MaterialAlertDialogBuilder] 替换默认的 ListPreference 对话框，
 * 以匹配 Material 3 的设计风格。
 */
class ListPreferenceM3DialogFragment : ListPreferenceDialogFragmentCompat() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(preference.dialogTitle)
            .setNegativeButton(preference.negativeButtonText, this)

        val view = context?.let { onCreateDialogView(it) }
        if (view == null) {
            builder.setMessage(preference.dialogMessage)
        } else {
            onBindDialogView(view)
            builder.setView(view)
        }
        onPrepareDialogBuilder(builder)
        return builder.create()
    }

    companion object {
        /**
         * 显示 ListPreference 对话框
         *
         * @param instance 要显示的 ListPreference 实例
         * @param parent 父 Fragment
         */
        fun show(instance: ListPreference, parent: Fragment) {
            val fragment = ListPreferenceM3DialogFragment()
            fragment.arguments = bundleOf("key" to instance.key)
            fragment.setTargetFragment(parent, 0)
            fragment.show(parent.parentFragmentManager, "androidx.preference.PreferenceFragment.DIALOG")
        }
    }
}
