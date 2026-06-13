@file:Suppress("DEPRECATION")

package com.zhufucdev.motion_emulator.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.CellInfo
import android.telephony.CellLocation
import android.telephony.NeighboringCellInfo
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.zhufucdev.me.stub.CellMoment
import com.zhufucdev.me.stub.CellTimeline
import java.util.Timer
import java.util.concurrent.Executor
import kotlin.concurrent.timer
import kotlin.reflect.full.memberFunctions

/**
 * 电话数据录制器（单例）
 *
 * 负责管理基站信息的录制，支持：
 * - 基站信息变化监听
 * - 基站位置变化监听
 * - 邻近基站信息采集
 * - 时间接近的 CellMoment 合并
 *
 * ## 合并策略
 * 当两个 CellMoment 的时间差小于 50ms 且类型不同时，会将它们合并为一个。
 * 这确保了同一时刻采集的多种基站信息被正确关联。
 */
object TelephonyRecorder {
    private lateinit var manager: TelephonyManager
    fun init(context: Context) {
        manager = context.getSystemService(TelephonyManager::class.java)
        checkPermission = {
            context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    && context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 合并时间接近的 CellMoment
     *
     * 如果最后一个 CellMoment 的时间差小于 50ms 且类型不同，
     * 则将两者合并；否则直接添加到 timeline
     *
     * @param timeline 当前的时间线列表
     * @param moment 要添加的新 CellMoment
     * @return 最终添加到 timeline 的 CellMoment（可能是合并后的）
     */
    private fun mergeIfPossible(timeline: ArrayList<CellMoment>, moment: CellMoment): CellMoment {
        synchronized(TelephonyRecorder::class.java) {
            if (timeline.isNotEmpty() &&
                Math.abs(moment.elapsed - timeline.last().elapsed) <= 0.05f
            ) {
                val last = timeline.last()
                if (!last.isSameTypeOf(moment)) {
                    timeline.removeLast()
                    val merged = last.merge(moment)
                    timeline.add(merged)
                    return merged
                }
            }
            timeline.add(moment)
            return moment
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun start(): TelephonyRecordCallback {
        if (!checkPermission()) {
            return noop()
        }

        val start = System.currentTimeMillis()
        val timeline = arrayListOf<CellMoment>()

        var updateListener: ((CellMoment) -> Unit)? = null
        fun elapsed(): Float = (System.currentTimeMillis() - start) / 1000F
        val cancel: () -> Unit

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val telephonyCallback = object : TelephonyCallback(), TelephonyCallback.CellInfoListener, TelephonyCallback.CellLocationListener {
                override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>) {
                    val moment = mergeIfPossible(timeline, CellMoment(elapsed(), cellInfo))
                    updateListener?.invoke(moment)
                }

                override fun onCellLocationChanged(location: CellLocation) {
                    val moment = mergeIfPossible(timeline, CellMoment(elapsed(), location = location))
                    updateListener?.invoke(moment)
                }
            }

            manager.registerTelephonyCallback(mainExecutor, telephonyCallback)
            cancel = {
                manager.unregisterTelephonyCallback(telephonyCallback)
            }
        } else {
            val listener = object : PhoneStateListener() {
                @Deprecated("Deprecated in Java")
                override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
                    if (cellInfo != null) {
                        val moment = mergeIfPossible(timeline, CellMoment(elapsed(), cellInfo))
                        updateListener?.invoke(moment)
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onCellLocationChanged(location: CellLocation?) {
                    if (location != null) {
                        val moment = mergeIfPossible(timeline, CellMoment(elapsed(), location = location))
                        updateListener?.invoke(moment)
                    }
                }
            }
            var timer: Timer? = null
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val method =
                    TelephonyManager::class.memberFunctions.firstOrNull { it.name.startsWith("getNeighboringCellInfo") }
                if (method == null) {
                    Log.w("telephony recorder", "method to get neighboring cell info isn't available")
                } else {
                    timer = timer("neighboring daemon", period = 1500L) {
                        val infos = method.call(manager) as List<NeighboringCellInfo>? ?: return@timer
                        val moment = mergeIfPossible(timeline, CellMoment(elapsed(), neighboring = infos))
                        updateListener?.invoke(moment)
                    }
                }
            }

            manager.listen(
                listener,
                PhoneStateListener.LISTEN_CELL_INFO
                        or PhoneStateListener.LISTEN_CELL_LOCATION
            )
            cancel = {
                manager.listen(listener, PhoneStateListener.LISTEN_NONE)
                timer?.cancel()
            }
        }

        return object : TelephonyRecordCallback {
            override fun onUpdate(l: (CellMoment) -> Unit) {
                updateListener = l
            }

            override fun summarize(): CellTimeline {
                cancel()
                return CellTimeline(NanoIdUtils.randomNanoId(), null, start, timeline)
            }
        }
    }

    private val mainExecutor = object : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable?) {
            handler.post {
                command?.run()
            }
        }
    }

    private lateinit var checkPermission: () -> Boolean
    private fun noop() = object : TelephonyRecordCallback {
        override fun onUpdate(l: (CellMoment) -> Unit) {
        }

        override fun summarize(): CellTimeline {
            return CellTimeline(NanoIdUtils.randomNanoId(), null, 0L, emptyList())
        }
    }
}


interface TelephonyRecordCallback {
    fun onUpdate(l: (CellMoment) -> Unit)
    fun summarize(): CellTimeline
}

fun CellMoment.isSameTypeOf(other: CellMoment): Boolean =
    cell.isEmpty() == other.cell.isEmpty()
            && neighboring.isEmpty() == other.neighboring.isEmpty()
            && (location == null) == (other.location == null)

fun CellMoment.merge(other: CellMoment): CellMoment {
    val rCell: List<CellInfo> = cell.takeIf { it.isNotEmpty() } ?: other.cell
    val rNeighboring: List<NeighboringCellInfo> =
        neighboring.takeIf { it.isNotEmpty() } ?: other.neighboring
    val rLocation: CellLocation? = location ?: other.location

    return CellMoment(elapsed, rCell, rNeighboring, rLocation)
}
