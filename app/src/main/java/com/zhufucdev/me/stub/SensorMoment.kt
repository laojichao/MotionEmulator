package com.zhufucdev.me.stub

import kotlinx.serialization.Serializable

@Serializable
data class SensorMoment(
    val elapsed: Float,
    val values: FloatArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SensorMoment) return false
        return elapsed == other.elapsed && values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        return elapsed.hashCode() * 31 + values.contentHashCode()
    }
}
