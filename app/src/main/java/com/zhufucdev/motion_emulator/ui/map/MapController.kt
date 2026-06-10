package com.zhufucdev.motion_emulator.ui.map

import android.content.Context
import android.location.Location
import android.view.MotionEvent
import android.view.View
import com.zhufucdev.motion_emulator.extension.dateString
import com.zhufucdev.motion_emulator.extension.effectiveTimeFormat
import com.zhufucdev.motion_emulator.extension.toPoint
import com.zhufucdev.motion_emulator.ui.DrawResult
import com.zhufucdev.motion_emulator.ui.DrawToolCallback
import com.zhufucdev.motion_emulator.ui.GpsToolCallback
import com.zhufucdev.me.stub.CoordinateSystem
import com.zhufucdev.me.stub.Point
import com.zhufucdev.me.stub.Trace

/**
 * 地图控制器抽象基类
 *
 * 提供地图操作的统一接口，支持 AMap 和 Google Maps 两种实现。
 * 包含轨迹绘制、GPS 录制、相机控制等功能。
 *
 * ## 主要功能
 * - 绑定相机到指定区域
 * - 在地图上绘制轨迹
 * - 地理编码（坐标转地址）
 * - 移动相机到指定位置
 * - 更新位置指示器
 * - 手动绘制轨迹（[useDraw]）
 * - GPS 录制轨迹（[useGps]）
 *
 * @param context Android Context
 */
abstract class MapController(val context: Context) {
    /** 地图显示样式（普通/夜间/卫星） */
    abstract var displayStyle: MapStyle

    /** 地图交互模式（静态/交互） */
    abstract var displayType: MapDisplayType

    /**
     * 绑定相机到指定的地理边界
     *
     * @param bounds 地理边界
     * @param animate 是否使用动画过渡
     */
    abstract fun boundCamera(bounds: TraceBounds, animate: Boolean = false)

    /**
     * 获取当前相机中心点的地理坐标
     *
     * @return 相机中心点的 [Point]
     */
    abstract fun cameraCenter(): Point

    /**
     * 在地图上绘制轨迹
     *
     * @param trace 要绘制的轨迹数据
     * @return 轨迹回调，可用于后续移除轨迹
     */
    abstract fun drawTrace(trace: Trace): MapTraceCallback

    /**
     * 反向地理编码 - 获取坐标对应的地址
     *
     * @param point 地理坐标
     * @return 地址字符串，如果无法获取则返回 null
     */
    abstract suspend fun getAddress(point: Point): String?

    /**
     * 移动相机到指定位置
     *
     * @param location 目标位置
     * @param animate 是否使用动画过渡
     * @param focus 是否聚焦（使用更高的缩放级别）
     */
    abstract fun moveCamera(location: Point, animate: Boolean = false, focus: Boolean = false)

    /**
     * 将屏幕坐标转换为地理坐标
     *
     * @param x 屏幕 X 坐标
     * @param y 屏幕 Y 坐标
     * @return 对应的地理坐标 [Point]
     */
    abstract fun project(x: Int, y: Int): Point

    /**
     * 更新位置指示器的位置
     *
     * @param location 新的位置信息
     */
    abstract fun updateLocationIndicator(location: Location)

    /**
     * 创建地图涂鸦/绘制实例
     *
     * @return [MapScrawl] 实例
     */
    abstract fun usePen(): MapScrawl

    /**
     * 创建手动绘制工具
     *
     * 在指定的 View 上监听触摸事件，将触摸轨迹转换为地理坐标点。
     * 支持撤销和清除操作。
     *
     * @param screen 用于接收触摸事件的 View
     * @return [DrawToolCallback] 实例
     */
    fun useDraw(screen: View): DrawToolCallback {
        val pen = usePen()
        screen.visibility = View.VISIBLE
        screen.setOnTouchListener { _, event ->
            if (event.pointerCount > 1) return@setOnTouchListener false
            if (event.action == MotionEvent.ACTION_DOWN) pen.markBegin()
            pen.addPoint(project(event.x.toInt(), event.y.toInt()))
            true
        }

        return object : DrawToolCallback {
            private var completeListener: ((DrawResult) -> Unit)? = null

            override fun clear() {
                pen.clear()
            }

            override suspend fun complete(): DrawResult {
                screen.visibility = View.GONE
                screen.setOnTouchListener(null)
                val points = pen.getPoints()
                if (points.isEmpty()) return DrawResult()

                val address = try {
                    getAddress(cameraCenter())
                } catch (_: Exception) { null }

                val name = address?.let {
                    context.getString(com.zhufucdev.motion_emulator.R.string.text_near, it)
                } ?: context.getSharedPreferences("settings", Context.MODE_PRIVATE).effectiveTimeFormat().dateString()

                val result = DrawResult(name, points, CoordinateSystem.WGS84)
                completeListener?.invoke(result)
                return result
            }

            override fun onCompleted(l: (DrawResult) -> Unit) {
                completeListener = l
            }

            override fun undo() {
                pen.undo()
            }
        }
    }

    /**
     * 创建 GPS 录制工具
     *
     * 通过 GPS 定位录制轨迹，支持暂停/恢复功能。
     * 会实际启动 GPS 定位并实时更新轨迹。
     *
     * @return [GpsToolCallback] 实例
     */
    suspend fun useGps(): GpsToolCallback {
        val pen = usePen()
        pen.markBegin()

        val locationManager = context.getSystemService(android.location.LocationManager::class.java)
        var locationListener: android.location.LocationListener? = null
        var resumed = false

        // 创建 GPS 工具回调
        val callback = object : GpsToolCallback {
            private var completeListener: ((DrawResult) -> Unit)? = null

            override fun isPaused(): Boolean = pen.element
            override fun pause() { pen.element = true }
            override fun unpause() {
                pen.markBegin()
                pen.element = false
            }

            override suspend fun complete(): DrawResult {
                // 移除位置监听器
                locationListener?.let { locationManager.removeUpdates(it) }

                val points = pen.getPoints()
                if (points.isEmpty()) return DrawResult()

                val address = try {
                    getAddress(points.first())
                } catch (_: Exception) { null }

                val name = address?.let {
                    context.getString(com.zhufucdev.motion_emulator.R.string.text_near, it)
                } ?: context.getSharedPreferences("settings", Context.MODE_PRIVATE).effectiveTimeFormat().dateString()

                val result = DrawResult(name, points, points.first().coordinateSystem)
                completeListener?.invoke(result)
                return result
            }

            override fun onCompleted(l: (DrawResult) -> Unit) {
                completeListener = l
            }

            override fun undo() {
                pen.undo()
            }
        }

        // 注册 GPS 位置监听器
        locationListener = object : android.location.LocationListener {
            override fun onLocationChanged(location: android.location.Location) {
                val point = com.zhufucdev.motion_emulator.extension.toPoint(location)
                if (!pen.element) {
                    pen.addPoint(point)
                }
                // 首次定位时移动相机
                if (!resumed) {
                    resumed = true
                    moveCamera(point, animate = true, focus = true)
                }
                updateLocationIndicator(location)
            }
        }

        // 请求位置更新
        try {
            locationManager.requestLocationUpdates(
                android.location.LocationManager.GPS_PROVIDER,
                0L, 0.5f, locationListener!!
            )
        } catch (e: SecurityException) {
            // 权限不足时忽略
        }

        return callback
    }
}
