package com.zhufucdev.motion_emulator.ui

import com.zhufucdev.me.stub.CoordinateSystem
import com.zhufucdev.me.stub.Point

/**
 * 绘制工具结果数据类
 *
 * 包含手动绘制或 GPS 录制产生的轨迹数据
 *
 * @property poiName 兴趣点名称（如地址或日期字符串）
 * @property trace 轨迹点列表
 * @property coordinateSystem 坐标系统（如 WGS84 或 GCJ02）
 */
data class DrawResult(
    val poiName: String = "",
    val trace: List<Point> = emptyList(),
    val coordinateSystem: CoordinateSystem = CoordinateSystem.WGS84
) : ToolCallbackResult
