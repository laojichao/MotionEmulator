package com.zhufucdev.motion_emulator.extension

import com.zhufucdev.me.stub.BlockBox
import com.zhufucdev.me.stub.Box
import com.zhufucdev.me.stub.Data
import com.zhufucdev.me.stub.EmptyBox

/**
 * 创建包含指定值的 Box
 *
 * @param T 数据类型，必须实现 [Data]
 * @param value 要包装的值
 * @return 包含 [value] 的 [Box] 实例
 */
@Suppress("UNCHECKED_CAST")
fun <T : Data> box(value: T): Box<T> {
    return Box(value) as Box<T>
}

/**
 * 创建空 Box
 *
 * @param T 数据类型，必须实现 [Data]
 * @return 空的 [EmptyBox] 实例
 */
@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
inline fun <T : Data> emptyBox(): Box<T> {
    return EmptyBox<T>() as Box<T>
}

/**
 * 创建阻塞 Box
 *
 * @param T 数据类型，必须实现 [Data]
 * @return 阻塞的 [BlockBox] 实例
 */
@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
inline fun <T : Data> blockBox(): Box<T> {
    return BlockBox<T>() as Box<T>
}
