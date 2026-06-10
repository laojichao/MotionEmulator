package com.zhufucdev.motion_emulator.ui.model

import com.zhufucdev.me.stub.Data
import com.zhufucdev.me.stub.Emulation
import com.zhufucdev.motion_emulator.data.Telephonies
import com.zhufucdev.motion_emulator.data.Motions
import com.zhufucdev.motion_emulator.data.Traces
import com.zhufucdev.motion_emulator.extension.StoredBox
import kotlinx.serialization.Serializable
import java.io.OutputStream
import java.text.DateFormat

@Serializable
data class EmulationRef(
    override val id: String,
    val name: String,
    val trace: String,
    val motion: String,
    val cells: String,
    val velocity: Double,
    val repeat: Int,
    val satelliteCount: Int,
) : Data {
    override fun getDisplayName(format: DateFormat): String = name
    override fun writeTo(stream: OutputStream) {}
}

fun EmulationRef.emulation(): Emulation? {
    val t = Traces[trace]?.value ?: return null
    return Emulation(
        t,
        StoredBox(motion, Motions),
        StoredBox(cells, Telephonies),
        velocity,
        repeat,
        satelliteCount
    )
}