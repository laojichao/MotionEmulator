package com.zhufucdev.motion_emulator.ui.map

import android.content.Context
import com.amap.api.services.poisearch.PoiSearchV2
import com.zhufucdev.motion_emulator.extension.defaultKtorClient
import com.zhufucdev.motion_emulator.extension.toFixed
import com.zhufucdev.motion_emulator.extension.toPoint
import com.zhufucdev.me.stub.Point
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * 高德地图 POI 搜索引擎
 *
 * 使用高德地图 API 实现 POI 搜索功能，支持：
 * - 坐标反向查询（坐标转地址）
 * - 文本搜索（关键词搜索 POI）
 *
 * @param context Android Context
 */
class AMapPoiEngine(private val context: Context) : PoiSearchEngine {

    /**
     * 根据坐标搜索 POI
     *
     * 使用高德地图反向地理编码 API，将坐标转换为地址信息
     *
     * @param point 地理坐标
     * @return 对应的 [Poi]，如果未找到则返回 null
     */
    override suspend fun search(point: Point): Poi? {
        val response = defaultKtorClient.get("https://restapi.amap.com/v3/geocode/regeo") {
            parameter("key", "0f605c48cca0e070f2342f4ba7af4b18")
            parameter("location", "${point.longitude.toFloat().toFixed(6)},${point.latitude.toFloat().toFixed(6)}")
        }
        if (response.status.value !in 200..299) return null
        val res = response.body<JsonObject>()
        if (res["status"]?.jsonPrimitive?.int != 1
            || res["info"]?.jsonPrimitive?.content != "OK"
        ) return null

        val info = res["regeocode"]!!.jsonObject["addressComponent"]!!.jsonObject
        return Poi(
            city = info["city"].toString(),
            province = info["province"].toString(),
            name = res["regeocode"]!!.jsonObject["formatted_address"].toString(),
            location = point
        )
    }

    /**
     * 根据文本搜索 POI 列表
     *
     * 使用高德地图 POI 搜索 API
     *
     * @param text 搜索关键词
     * @param limit 最大返回数量
     * @return 匹配的 [Poi] 列表
     */
    override suspend fun search(text: String, limit: Int): List<Poi> = suspendCancellableCoroutine { res ->
        val query = PoiSearchV2.Query(text, null)
        query.pageSize = limit
        val search = PoiSearchV2(context, query)
        search.setOnPoiSearchListener(object : PoiSearchV2.OnPoiSearchListener {
            override fun onPoiSearched(p0: com.amap.api.services.poisearch.PoiResultV2?, p1: Int) {
                if (p0 == null) {
                    res.resumeWith(Result.failure(NullPointerException("PoiResultV2")))
                    return
                }
                res.resumeWith(Result.success(p0.pois.map {
                    Poi(it.cityName, it.provinceName, it.title, it.latLonPoint.toPoint())
                }))
            }

            override fun onPoiItemSearched(p0: com.amap.api.services.core.PoiItemV2?, p1: Int) {}
        })
        search.searchPOIAsyn()
    }
}
