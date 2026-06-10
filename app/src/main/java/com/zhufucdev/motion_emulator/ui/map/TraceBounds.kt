package com.zhufucdev.motion_emulator.ui.map

import android.content.Context
import com.zhufucdev.motion_emulator.extension.ensureAmapCoordinate
import com.zhufucdev.motion_emulator.extension.ensureGoogleCoordinate
import com.zhufucdev.motion_emulator.extension.toAmapLatLng
import com.zhufucdev.motion_emulator.extension.toGoogleLatLng
import com.zhufucdev.motion_emulator.extension.toPoint
import com.zhufucdev.me.stub.CoordinateSystem
import com.zhufucdev.me.stub.Point
import com.zhufucdev.me.stub.Trace

/**
 * 轨迹边界数据类
 *
 * 表示轨迹的地理边界矩形，包含东北角和西南角两个坐标点。
 * 用于相机绑定和自动缩放。
 *
 * @property northeast 东北角坐标
 * @property southwest 西南角坐标
 */
data class TraceBounds(val northeast: Point, val southwest: Point)

/**
 * 从 Trace 计算地理边界
 *
 * 根据轨迹的坐标系统选择 AMap 或 Google Maps 的边界计算方式
 *
 * @param trace 轨迹数据
 * @return 包含轨迹边界的 [TraceBounds]
 */
fun TraceBounds(trace: Trace): TraceBounds {
    return if (trace.coordinateSystem == CoordinateSystem.WGS84) {
        val builder = com.google.android.gms.maps.model.LatLngBounds.builder()
        trace.points.forEach {
            builder.include(it.toGoogleLatLng())
        }
        val result = builder.build()
        TraceBounds(result.northeast.toPoint(), result.southwest.toPoint())
    } else {
        val builder = com.amap.api.maps.model.LatLngBounds.builder()
        trace.points.forEach {
            builder.include(it.toAmapLatLng())
        }
        val result = builder.build()
        TraceBounds(result.northeast.toPoint(), result.southwest.toPoint())
    }
}

/**
 * 转换为 AMap 的 LatLngBounds
 *
 * @param context Android Context，用于坐标转换
 * @return AMap 的 LatLngBounds 对象
 */
fun TraceBounds.amap(context: Context) =
    com.amap.api.maps.model.LatLngBounds(southwest.ensureAmapCoordinate(context).toAmapLatLng(), northeast.ensureAmapCoordinate(context).toAmapLatLng())

/**
 * 转换为 Google Maps 的 LatLngBounds
 *
 * @return Google Maps 的 LatLngBounds 对象
 */
fun TraceBounds.google() =
    com.google.android.gms.maps.model.LatLngBounds(southwest.ensureGoogleCoordinate().toGoogleLatLng(), northeast.ensureGoogleCoordinate().toGoogleLatLng())