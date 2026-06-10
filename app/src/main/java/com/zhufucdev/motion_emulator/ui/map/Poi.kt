package com.zhufucdev.motion_emulator.ui.map

import com.zhufucdev.me.stub.Point

/**
 * 兴趣点（Point of Interest）数据类
 *
 * 表示地图上的一个兴趣点，包含位置信息和地理描述
 *
 * @property city 城市名称
 * @property province 省份/州名称
 * @property name 兴趣点名称
 * @property location 地理坐标位置
 */
data class Poi(
    val city: String,
    val province: String,
    val name: String,
    val location: Point
)
