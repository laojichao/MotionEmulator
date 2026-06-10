package com.zhufucdev.motion_emulator.ui.map

import com.zhufucdev.me.stub.Point

/**
 * 地图涂鸦/绘制接口
 *
 * 提供在地图上绘制轨迹的能力，支持添加点、撤销、清除等操作。
 * 由 [MapController.usePen] 创建。
 */
interface MapScrawl {
    /**
     * 暂停/恢复标记
     *
     * 当为 true 时，新添加的点不会连接到之前的轨迹
     */
    var element: Boolean

    /**
     * 标记新轨迹段的开始
     *
     * 调用后，后续添加的点将形成新的轨迹段，与之前的点断开连接
     */
    fun markBegin()

    /**
     * 添加一个点到当前轨迹
     *
     * @param point 要添加的地理坐标点
     */
    fun addPoint(point: Point)

    /**
     * 获取当前轨迹的所有点
     *
     * @return 轨迹点列表
     */
    fun getPoints(): List<Point>

    /**
     * 撤销最后一次操作
     */
    fun undo()

    /**
     * 清除所有轨迹点
     */
    fun clear()
}
