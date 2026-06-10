package com.zhufucdev.motion_emulator.ui.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.zhufucdev.me.stub.Point
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Google Maps POI 搜索引擎
 *
 * 使用 Android Geocoder API 实现 POI 搜索功能，支持：
 * - 坐标反向查询（坐标转地址）
 * - 文本搜索（关键词搜索 POI）
 *
 * @param context Android Context
 */
class GooglePoiEngine(private val context: Context) : PoiSearchEngine {

    /**
     * 将 Address 转换为 Poi
     *
     * @param address Android 地址对象
     * @return 对应的 [Poi]
     */
    private fun poi(address: Address): Poi {
        val city = address.subAdminArea ?: address.getAddressLine(0) ?: ""
        val province = address.adminArea ?: ""
        val name = address.subThoroughfare ?: address.featureName ?: ""
        return Poi(city, province, name, Point(address.latitude, address.longitude))
    }

    /**
     * 根据坐标搜索 POI
     *
     * 使用 Geocoder 反向地理编码
     *
     * @param point 地理坐标
     * @return 对应的 [Poi]，如果未找到则返回 null
     */
    override suspend fun search(point: Point): Poi? {
        if (!Geocoder.isPresent()) return null
        val geocoder = Geocoder(context)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { cont ->
                geocoder.getFromLocation(point.latitude, point.longitude, 1) {
                    cont.resumeWith(Result.success(it.firstOrNull()?.let { addr -> poi(addr) }))
                }
            }
        } else {
            @Suppress("DEPRECATION")
            geocoder.getFromLocation(point.latitude, point.longitude, 1)?.firstOrNull()?.let { poi(it) }
        }
    }

    /**
     * 根据文本搜索 POI 列表
     *
     * 使用 Geocoder 文本搜索
     *
     * @param text 搜索关键词
     * @param limit 最大返回数量
     * @return 匹配的 [Poi] 列表
     */
    override suspend fun search(text: String, limit: Int): List<Poi> {
        if (!Geocoder.isPresent()) return emptyList()
        val geocoder = Geocoder(context)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { cont ->
                geocoder.getFromLocationName(text, limit) { addresses ->
                    cont.resumeWith(Result.success(addresses.map { poi(it) }))
                }
            }
        } else {
            @Suppress("DEPRECATION")
            (geocoder.getFromLocationName(text, limit) ?: emptyList()).map { poi(it) }
        }
    }
}
