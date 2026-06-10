package com.zhufucdev.motion_emulator.ui.map

/**
 * 地图轨迹回调接口
 *
 * 当在地图上绘制轨迹后返回此接口，用于控制已绘制轨迹的生命周期
 */
fun interface MapTraceCallback {
    /**
     * 从地图上移除已绘制的轨迹
     */
    fun remove()
}
