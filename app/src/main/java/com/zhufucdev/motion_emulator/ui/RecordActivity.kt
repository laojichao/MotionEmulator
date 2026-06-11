package com.zhufucdev.motion_emulator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.zhufucdev.motion_emulator.R
import com.zhufucdev.motion_emulator.data.MotionRecorder
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
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment_activity_record)
        initializeToolbar(this, binding.appBarToolbar, navController)
        setUpStatusBar(this)
    }
}
