package com.zhufucdev.motion_emulator.ui

/**
 * 移动工具回调（单例）
 *
 * 实现 [ToolCallback] 接口，用于地图的平移和缩放操作。
 * 所有方法均为空实现，因为移动工具不产生轨迹数据。
 */
object MoveToolCallback : ToolCallback<MoveResult> {
    override suspend fun complete(): MoveResult = MoveResult
    override fun onCompleted(l: (MoveResult) -> Unit) {}
    override fun undo() {}
}
