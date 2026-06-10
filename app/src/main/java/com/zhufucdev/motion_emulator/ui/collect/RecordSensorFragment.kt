package com.zhufucdev.motion_emulator.ui.collect

import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.materialswitch.MaterialSwitch
import com.zhufucdev.motion_emulator.R
import com.zhufucdev.motion_emulator.databinding.FragmentRecordSensorBinding

class RecordSensorFragment : Fragment() {
    private lateinit var binding: FragmentRecordSensorBinding
    private lateinit var sm: SensorManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordSensorBinding.inflate(layoutInflater, container, false)
        sm = requireContext().getSystemService(SensorManager::class.java)

        binding.btnContinue.setOnClickListener {
            findNavController().navigate(
                R.id.action_recordSensorFragment_to_recordDataFragment,
                bundleOf(
                    "types" to getSensorTypes(),
                    "telephony" to binding.switchTelephony.isChecked
                )
            )
        }

        return binding.root
    }

    private fun getSensorTypes(): ArrayList<Int> {
        val map = mapOf(
            1 to binding.switchAcc,
            18 to binding.switchStepDetec,
            19 to binding.switchStepCounter,
            4 to binding.switchGyroscope,
            16 to binding.switchGyroscope,
            2 to binding.switchMagnetic,
            14 to binding.switchMagnetic,
            10 to binding.switchLinearAcc,
            5 to binding.switchLight
        ).toMutableMap()

        if (Build.VERSION.SDK_INT >= 26) {
            map[35] = binding.switchAcc
        }

        return ArrayList(
            map.entries
                .filter { (type, switch) ->
                    switch.isChecked && sm.getDefaultSensor(type) != null
                }
                .map { it.key }
        )
    }
}
