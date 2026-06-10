package com.zhufucdev.motion_emulator.ui

/**
 * 手动绘制工具回调接口
 *
 * 继承自 [ToolCallback]，用于在地图上手动绘制轨迹。
 * 支持清除已绘制的轨迹。
 */
interface DrawToolCallback : ToolCallback<DrawResult> {
    /**
     * 清除所有已绘制的轨迹点
     */
    fun clear()
}
