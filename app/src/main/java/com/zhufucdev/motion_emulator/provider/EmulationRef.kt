package com.zhufucdev.motion_emulator.provider

import kotlinx.serialization.Serializable

/**
 * 模拟配置引用
 *
 * 存储模拟配置中各组件的引用 ID，用于持久化存储默认配置。
 * 当用户保存默认配置时，会将当前模拟参数转换为 [EmulationRef] 并存储。
 *
 * @property trace 轨迹数据的引用 ID
 * @property motion 运动数据的引用 ID，"none" 表示无运动数据，"block" 表示阻塞
 * @property cells 基站数据的引用 ID，"none" 表示无基站数据，"block" 表示阻塞
 * @property velocity 模拟速度（米/秒）
 * @property repeat 重复次数
 * @property satelliteCount 卫星数量
 */
@Serializable
data class EmulationRef(
    val trace: String = "",
    val motion: String = "",
    val cells: String = "",
    val velocity: Double = 0.0,
    val repeat: Int = 0,
    val satelliteCount: Int = 0
)
