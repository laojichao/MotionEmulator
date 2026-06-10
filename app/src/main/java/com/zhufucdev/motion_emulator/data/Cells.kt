package com.zhufucdev.motion_emulator.data

import com.zhufucdev.me.stub.CellTimeline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * 基站数据存储（单例）
 *
 * 继承自 [DataStore]，专门用于管理 [CellTimeline] 类型的数据。
 * 基站数据包含手机信号塔的连接信息，用于模拟真实的基站环境。
 *
 * ## 数据类型
 * - 类型名称: "cells"
 * - 序列化器: [CellTimeline.serializer]
 * - 数据类: [CellTimeline]
 */
object Cells : DataStore<CellTimeline>() {
    override val typeName: String get() = "cells"
    override val clazz: KClass<CellTimeline> = CellTimeline::class
    override val dataSerializer: KSerializer<CellTimeline> = serializer()
}
