package com.zhufucdev.motion_emulator.data

import com.zhufucdev.me.stub.CellTimeline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * 电话数据存储（单例）
 *
 * 继承自 [DataStore]，用于管理 [CellTimeline] 类型的电话数据。
 * 与 [Cells] 不同，此存储专门用于录制的电话数据。
 *
 * ## 数据类型
 * - 类型名称: "telephony"
 * - 数据类: [CellTimeline]
 */
object Telephonies : DataStore<CellTimeline>() {
    override val typeName: String get() = "telephony"
    override val dataSerializer: KSerializer<CellTimeline> = serializer()
}
