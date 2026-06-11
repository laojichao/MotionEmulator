package com.zhufucdev.motion_emulator.ui.collect

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.zhufucdev.motion_emulator.R
import com.zhufucdev.motion_emulator.data.Cells
import com.zhufucdev.motion_emulator.data.DataStore
import com.zhufucdev.motion_emulator.data.MotionCallback
import com.zhufucdev.motion_emulator.data.MotionRecorder
import com.zhufucdev.motion_emulator.data.Motions
import com.zhufucdev.motion_emulator.data.TelephonyRecordCallback
import com.zhufucdev.motion_emulator.data.TelephonyRecorder
import com.zhufucdev.motion_emulator.extension.getAttrColor
import com.zhufucdev.me.stub.CellMoment
import com.zhufucdev.me.stub.MotionMoment

class RecordDataFragment : Fragment() {
    private lateinit var fab: ExtendedFloatingActionButton
    private lateinit var motion: MotionCallback
    private var telephony: TelephonyRecordCallback? = null
    private lateinit var types: ArrayList<Int>
    private var useTelephony = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        types = arguments?.getIntegerArrayList("types") ?: defaultSensors()
        useTelephony = arguments?.getBoolean("telephony") ?: false
        motion = MotionRecorder.start(types)
        if (useTelephony) {
            telephony = TelephonyRecorder.start()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_record_data, container, false)
        val recordContainer = view.findViewById<LinearLayoutCompat>(R.id.record_container)
        fab = view.findViewById(R.id.btn_stop)

        for (type in types) {
            recordContainer.addView(generateChart(type))
        }
        if (useTelephony) {
            recordContainer.addView(telephonyChart())
        }

        fab.setOnClickListener {
            val motionResult = motion.summarize()
            Motions.store(motionResult)
            if (useTelephony) {
                val cellTimeline = telephony!!.summarize()
                Cells.store(cellTimeline)
            }
            requireActivity().finish()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        motion.summarize()
    }

    private fun generateChart(type: Int): View {
        val data = LineData()
        val chart = LineChart(requireContext())
        stylize(chart)
        chart.description.text = getString(sensorNames[type]!!)

        motion.onUpdate(type) { moment ->
            val values = moment.data[type]!!
            if (data.dataSets.isEmpty()) {
                values.forEachIndexed { index, value ->
                    val label = if (index < sensorValueLabels.size) {
                        getString(sensorValueLabels[index])
                    } else {
                        "U"
                    }
                    data.addDataSet(
                        LineDataSet(
                            arrayListOf(Entry(moment.elapsed, value)),
                            label
                        )
                    )
                }
            } else {
                values.forEachIndexed { index, value ->
                    data.addEntry(Entry(moment.elapsed, value), index)
                }
                while ((data.dataSets.first() as LineDataSet).entryCount > 50) {
                    data.dataSets.forEach { (it as LineDataSet).removeEntry(0) }
                }
            }
            requireActivity().runOnUiThread {
                data.notifyDataChanged()
                chart.data = data
                chart.invalidate()
            }
        }

        chart.data = data
        chart.invalidate()
        layout(chart)
        return chart
    }

    private fun layout(view: View) {
        view.layoutParams = LinearLayoutCompat.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            view.resources.getDimensionPixelSize(R.dimen.chart_height)
        ).apply {
            val margin = view.resources.getDimensionPixelSize(R.dimen.card_margin)
            setMargins(margin, margin, margin, margin)
        }
    }

    private fun stylize(chart: BarLineChartBase<*>) {
        val color = getAttrColor(com.google.android.material.R.attr.colorOnSurface, requireContext())
        chart.setBorderColor(color)
        chart.description.textColor = color
        chart.legend.textColor = color
        listOf(chart.axisLeft, chart.xAxis).forEach { axis ->
            axis.textColor = color
            axis.axisLineColor = color
        }
        chart.axisRight.isEnabled = false
    }

    private fun telephonyChart(): View {
        if (Build.VERSION.SDK_INT >= 30) {
            val container = LinearLayoutCompat(requireContext()).apply {
                orientation = LinearLayoutCompat.VERTICAL
            }
            val title = TextView(requireContext())
            title.text = getString(R.string.title_telephony_recording, "-1")
            container.addView(title)

            val chart = BarChart(requireContext())
            telephony!!.onUpdate { moment ->
                chart.data = generateBarData(moment)
                chart.invalidate()
                title.text = getString(R.string.title_telephony_recording, moment.elapsed.toString())
            }
            stylize(chart)
            chart.legend.isEnabled = false
            chart.description.text = getString(R.string.name_cell_signal)
            chart.setFitBars(true)
            layout(chart)
            container.addView(chart)
            return container
        }

        val title = TextView(requireContext())
        title.text = getString(R.string.title_telephony_recording, "-1")
        var containsLocation = false
        telephony!!.onUpdate { moment ->
            title.text = buildString {
                append(getString(R.string.title_telephony_recording, moment.elapsed.toString()))
                append('\n')
                append(getString(R.string.text_telephony_recording_neighboring, moment.neighboring.size))
                append('\n')
                if (moment.location != null || containsLocation) {
                    append(getString(R.string.text_telephony_recording_location))
                    append('\n')
                    containsLocation = true
                }
            }
        }
        return title
    }

    companion object {
        private fun generateBarData(moment: CellMoment): BarData {
            val entries = moment.cell.mapIndexed { index, cellInfo ->
                BarEntry(index.toFloat(), cellInfo.cellSignalStrength.dbm.toFloat())
            }
            return BarData(BarDataSet(entries, "BarDataSet"))
        }

        val sensorNames = mapOf(
            1 to R.string.name_sensor_acc,
            4 to R.string.name_sensor_gyroscope,
            2 to R.string.name_sensor_magnetic,
            5 to R.string.name_sensor_light,
            10 to R.string.name_sensor_linear_acc,
            14 to R.string.name_sensor_magnetic_uncal,
            16 to R.string.name_sensor_gyroscope_uncal,
            18 to R.string.name_sensor_step_detec,
            19 to R.string.name_sensor_step_counter
        )

        val sensorValueLabels = listOf(
            R.string.name_x,
            R.string.name_y,
            R.string.name_z
        )

        fun defaultSensors() = arrayListOf(1, 4, 2, 5, 10, 18, 19)
    }
}
