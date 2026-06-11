package com.zhufucdev.motion_emulator.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.zhufucdev.me.stub.Motion
import com.zhufucdev.me.stub.MotionMoment

/**
 * 运动数据录制回调接口
 *
 * 用于接收传感器数据更新通知和获取录制结果
 */
interface MotionCallback {
    /**
     * 获取录制结果并停止录制
     *
     * 注销传感器监听器，汇总所有录制的传感器数据，
     * 生成 [Motion] 对象并返回
     *
     * @return 包含所有录制数据的 [Motion] 对象
     */
    fun summarize(): Motion

    /**
     * 注册通用数据更新监听器
     *
     * 当所有传感器类型都有数据时触发回调
     *
     * @param l 回调函数，接收 [MotionMoment] 数据
     */
    fun onUpdate(l: (MotionMoment) -> Unit)

    /**
     * 注册特定类型传感器数据更新监听器
     *
     * 仅当指定类型的传感器数据更新时触发回调
     *
     * @param type 传感器类型常量（如 [Sensor.TYPE_ACCELEROMETER]）
     * @param l 回调函数，接收 [MotionMoment] 数据
     */
    fun onUpdate(type: Int, l: (MotionMoment) -> Unit)
}

/**
 * 运动数据录制器（单例）
 *
 * 负责管理传感器监听和运动数据录制。支持同时录制多种传感器数据，
 * 并在传感器数据时间接近时合并为同一时刻的数据。
 *
 * ## 使用流程
 * 1. 调用 [init] 初始化传感器管理器
 * 2. 调用 [start] 开始录制，传入需要录制的传感器类型列表
 * 3. 通过返回的 [MotionCallback] 监听数据更新
 * 4. 调用 [MotionCallback.summarize] 获取录制结果
 *
 * ## 传感器合并策略
 * 当两个传感器事件的时间差小于 50ms 时，会将它们合并到同一个 [MotionMoment] 中，
 * 以模拟多传感器同时采样的效果。当所有传感器类型都有数据后，才会触发通用回调。
 */
object MotionRecorder {
    private lateinit var sensors: SensorManager
    private val callbacks = arrayListOf<MotionCallback>()

    /**
     * 初始化传感器管理器
     *
     * 必须在 [start] 之前调用
     *
     * @param context Android Context，用于获取系统服务
     */
    fun init(context: Context) {
        sensors = context.getSystemService(SensorManager::class.java)
    }

    /**
     * 开始录制传感器数据
     *
     * 为每个指定的传感器类型注册监听器，创建并返回 [MotionCallback] 实例
     *
     * @param sensorsRequired 需要录制的传感器类型列表（如 [Sensor.TYPE_ACCELEROMETER]）
     * @return [MotionCallback] 实例，用于监听数据更新和获取录制结果
     */
    fun start(sensorsRequired: List<Int>): MotionCallback {
        val start = System.currentTimeMillis()
        val moments = arrayListOf<MotionMoment>()
        var callbackListener: ((MotionMoment) -> Unit)? = null
        val typedListeners = hashMapOf<Int, (MotionMoment) -> Unit>()
        val sensorsInvolved = sensorsRequired.toList()
        val sensorCount = sensorsInvolved.size

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val elapsed = (System.currentTimeMillis() - start) / 1000F

                // 传感器数据合并逻辑：
                // 如果最后一个 moment 的时间差小于 50ms，且该传感器类型尚未记录，
                // 则将数据合并到同一个 MotionMoment 中
                if (moments.isNotEmpty()) {
                    val lastMoment = moments.last()
                    if (Math.abs(lastMoment.elapsed - elapsed) < 0.05f
                        && !lastMoment.data.containsKey(event.sensor.type)
                    ) {
                        lastMoment.data[event.sensor.type] = event.values
                        typedListeners[event.sensor.type]?.invoke(lastMoment)
                        // 当所有传感器类型都有数据时，触发通用回调
                        if (lastMoment.data.size == sensorCount) {
                            callbackListener?.invoke(lastMoment)
                        }
                        return
                    }
                }

                // 创建新的 MotionMoment
                val data = mutableMapOf<Int, FloatArray>()
                data[event.sensor.type] = event.values.clone()
                val moment = MotionMoment(elapsed, data)
                moments.add(moment)
                typedListeners[event.sensor.type]?.invoke(moment)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorsRequired.forEach {
            val sensor = sensors.getDefaultSensor(it)
            sensors.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        }

        val result = object : MotionCallback {
            override fun summarize(): Motion {
                sensors.unregisterListener(listener)
                synchronized(MotionRecorder) {
                    callbacks.remove(this)
                }

                return Motion(NanoIdUtils.randomNanoId(), null, start, moments, sensorsInvolved)
            }

            override fun onUpdate(l: (MotionMoment) -> Unit) {
                callbackListener = l
            }

            override fun onUpdate(type: Int, l: (MotionMoment) -> Unit) {
                typedListeners[type] = l
            }
        }

        synchronized(this) {
            callbacks.add(result)
        }

        return result
    }
}
