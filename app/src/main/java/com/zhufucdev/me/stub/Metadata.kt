package com.zhufucdev.me.stub

/**
 * 数据元信息
 *
 * 存储数据条目的元信息，包括名称和创建时间。
 * 用于在 DataStore 中管理数据条目的显示和排序。
 *
 * @property name 数据名称，可为 null
 * @property creationTime 创建时间戳（毫秒），默认为当前时间
 */
data class Metadata(
    val name: String? = null,
    val creationTime: Long = System.currentTimeMillis()
)
