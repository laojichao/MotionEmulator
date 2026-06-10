package com.zhufucdev.motion_emulator.ui

/**
 * GPS 录制工具回调接口
 *
 * 继承自 [ToolCallback]，用于通过 GPS 定位录制轨迹。
 * 支持暂停和恢复录制。
 */
interface GpsToolCallback : ToolCallback<DrawResult> {
    /**
     * 检查录制是否已暂停
     *
     * @return true 表示已暂停，false 表示正在录制
     */
    fun isPaused(): Boolean

    /**
     * 暂停 GPS 录制
     */
    fun pause()

    /**
     * 恢复 GPS 录制
     */
    fun unpause()
}
