package com.zhufucdev.motion_emulator.ui.map

import com.zhufucdev.me.stub.Point

/**
 * POI（兴趣点）搜索引擎接口
 *
 * 定义地图 POI 搜索的统一接口，支持 AMap 和 Google Maps 两种实现。
 * 提供坐标反向查询和文本搜索两种方式。
 */
interface PoiSearchEngine {
    /**
     * 根据坐标搜索 POI
     *
     * @param point 地理坐标
     * @return 对应的 [Poi]，如果未找到则返回 null
     */
    suspend fun search(point: Point): Poi?

    /**
     * 根据文本搜索 POI 列表
     *
     * @param text 搜索关键词
     * @limit 最大返回数量
     * @return 匹配的 [Poi] 列表
     */
    suspend fun search(text: String, limit: Int): List<Poi>
}
