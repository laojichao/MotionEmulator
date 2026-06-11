package com.zhufucdev.motion_emulator.data

import com.zhufucdev.me.stub.CellTimeline
import com.zhufucdev.me.stub.Data
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import java.io.OutputStream
import java.text.DateFormat

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

/**
 * 电话数据组合存储（单例）
 *
 * 管理 [TelephonyComposite] 类型的数据，用于组合多个基站时间线
 *
 * ## 数据类型
 * - 类型名称: "telephony_composite"
 * - 数据类: [TelephonyComposite]
 */
object TelephonyComposites : DataStore<TelephonyComposite>() {
    override val typeName: String
        get() = "telephony_composite"
    override val dataSerializer: KSerializer<TelephonyComposite> = serializer()
}

/**
 * 电话数据组合
 *
 * 将多个基站时间线组合为一个逻辑单元，通过引用 ID 列表
 * 关联到实际的 [CellTimeline] 数据
 *
 * @property id 唯一标识符
 * @property name 显示名称
 * @property ref 引用的基站时间线 ID 列表
 */
@Serializable
data class TelephonyComposite(
    override val id: String,
    val name: String,
    private val ref: List<String>
) : Data {
    /** 懒加载的时间线列表，通过引用 ID 从 [Telephonies] 中获取 */
    val timelines by lazy { ref.map { Telephonies[it] } }

    override fun getDisplayName(format: DateFormat): String = name
    override fun writeTo(stream: OutputStream) {}
}
