package com.zhufucdev.motion_emulator.data

import android.content.Context
import com.zhufucdev.me.stub.Data
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.SortedMap

/**
 * 数据存储抽象基类
 *
 * 提供数据的持久化存储、读取、删除等操作。
 * 数据以 JSON 格式存储在应用的 files 目录下，文件名格式为 `{typeName}_{id}.json`。
 *
 * @param T 数据类型，必须实现 [Data] 接口
 */
abstract class DataStore<T : Data> {
    private val data: SortedMap<String, T> = sortedMapOf()
    private lateinit var rootDir: File

    /**
     * 数据类型名称，用于文件名前缀
     */
    abstract val typeName: String

    /**
     * 数据序列化器
     */
    protected abstract val dataSerializer: KSerializer<T>

    /**
     * 获取数据条目的存储文件名
     *
     * @param data 数据条目
     * @return 格式为 `{typeName}_{id}.json` 的文件名
     */
    private fun getStoreName(data: Data): String {
        return "${typeName}_${data.id}.json"
    }

    /**
     * 初始化数据存储
     *
     * 从文件系统加载所有已存储的数据条目，并移除不再存在的条目。
     * 必须在任何 I/O 操作之前调用。
     *
     * @param context Android Context，用于获取文件目录
     */
    fun require(context: Context) {
        rootDir = context.filesDir

        val files = rootDir.list()
        if (files == null) {
            data.clear()
            return
        }

        val existingIds = mutableListOf<String>()
        for (fileName in files) {
            val file = File(rootDir, fileName)
            if (!fileName.endsWith("json") || !fileName.startsWith(typeName)) continue

            val id = file.nameWithoutExtension.removePrefix("${typeName}_")
            existingIds.add(id)

            if (!data.containsKey(id)) {
                try {
                    val deserialized = FileInputStream(file).use { stream ->
                        Json.decodeFromStream(dataSerializer, stream)
                    }
                    data[deserialized.id] = deserialized
                } catch (e: Exception) {
                    // Skip corrupted files
                }
            }
        }

        // Remove entries that no longer have files
        val removed = data.keys.filter { it !in existingIds }
        removed.forEach { data.remove(it) }
    }

    /**
     * 存储数据条目
     *
     * 将数据写入文件系统并更新内存缓存。如果条目已存在且 [overwrite] 为 false，则跳过。
     *
     * @param record 要存储的数据
     * @param overwrite 是否覆盖已存在的条目
     */
    fun store(record: T, overwrite: Boolean = false) {
        val id = record.id
        if (data.containsKey(id) && !overwrite) return

        val file = File(rootDir, getStoreName(record))
        FileOutputStream(file).use { stream ->
            record.writeTo(stream)
        }
        data[id] = record
    }

    /**
     * 解析 JSON 字符串并存储
     *
     * @param json JSON 字符串
     * @param overwrite 是否覆盖已存在的条目
     * @return 解析后的数据对象
     */
    fun parseAndStore(json: String, overwrite: Boolean = false): T {
        val deserialized = Json.decodeFromString(dataSerializer, json)
        val id = deserialized.id
        if (data.containsKey(id) && !overwrite) {
            return deserialized
        }

        val file = File(rootDir, getStoreName(deserialized))
        file.writeText(json)
        data[id] = deserialized
        return deserialized
    }

    /**
     * 删除数据条目
     *
     * @param record 要删除的数据
     * @param context Android Context
     */
    fun delete(record: T, context: Context) {
        context.deleteFile(getStoreName(record))
        data.remove(record.id)
    }

    /**
     * 获取所有数据条目列表
     *
     * @return 数据条目列表
     */
    fun list(): List<T> = data.values.toList()

    /**
     * 根据 ID 获取数据条目
     *
     * @param id 数据条目 ID
     * @return 数据条目，如果不存在则返回 null
     */
    operator fun get(id: String): T? = data[id]

    override fun equals(other: Any?): Boolean {
        return other is DataStore<*> &&
                other.javaClass == this.javaClass &&
                other.typeName == this.typeName
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + rootDir.hashCode()
        result = 31 * result + typeName.hashCode()
        result = 31 * result + dataSerializer.hashCode()
        return result
    }
}
