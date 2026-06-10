package com.zhufucdev.motion_emulator.ui

/**
 * 工具回调接口（泛型）
 *
 * 定义轨迹绘制工具的通用行为，包括完成操作、监听完成事件和撤销。
 * 具体实现包括 [DrawToolCallback]（手动绘制）和 [GpsToolCallback]（GPS 录制）。
 *
 * @param T 工具结果类型，必须实现 [ToolCallbackResult]
 */
interface ToolCallback<T : ToolCallbackResult> {
    /**
     * 完成工具操作并返回结果
     *
     * @return 工具操作的结果
     */
    suspend fun complete(): T

    /**
     * 注册完成监听器
     *
     * 当工具操作完成时，会调用此监听器
     *
     * @param l 回调函数，接收操作结果
     */
    fun onCompleted(l: (T) -> Unit)

    /**
     * 撤销最后一次操作
     */
    fun undo()
}
