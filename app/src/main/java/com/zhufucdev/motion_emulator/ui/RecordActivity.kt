package com.zhufucdev.motion_emulator.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zhufucdev.motion_emulator.R
import com.zhufucdev.motion_emulator.data.Cells
import com.zhufucdev.motion_emulator.data.MotionRecorder
import com.zhufucdev.motion_emulator.data.Motions
import com.zhufucdev.motion_emulator.data.TelephonyRecorder
import com.zhufucdev.motion_emulator.databinding.ActivityRecordBinding
import com.zhufucdev.motion_emulator.extension.adjustToolbarMarginForNotch
import com.zhufucdev.motion_emulator.extension.initializeToolbar
import com.zhufucdev.motion_emulator.extension.setUpStatusBar

class RecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecordBinding

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        adjustToolbarMarginForNotch(this, binding.appBarLayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MotionRecorder.init(this)
        TelephonyRecorder.init(this)
        Motions.require(this)
        Cells.require(this)
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment_activity_record)
        initializeToolbar(this, binding.appBarToolbar, navController)
        setUpStatusBar(this)
        if (Build.VERSION.SDK_INT >= 29) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACTIVITY_RECOGNITION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.READ_PHONE_STATE
                ),
                0
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            val allGranted = grantResults.all { it == 0 }
            if (!allGranted) {
                Snackbar.make(binding.root, R.string.text_permission_not_granted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.title_permission_read) {
                        requirePermissions()
                    }.show()
            }
        }
    }

    private fun requirePermissions() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }
}
