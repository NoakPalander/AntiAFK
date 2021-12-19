package com.antiafk.core

import androidx.compose.material.Colors
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.antiafk.core.serializer.ColorSerializer
import com.antiafk.core.serializer.KeySerializer
import com.antiafk.core.serializer.PalleteSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.File

@Serializable
data class Config(
    @SerialName("keys")
    @Serializable(with = KeySerializer::class)
    val keys: SnapshotStateList<Pair<Int, String>>? = null,

    @SerialName("pallet")
    @Serializable(with = PalleteSerializer::class)
    val pallet: Colors? = null,

    @Transient
    var saveFile: File? = null
) {
    companion object {
        fun load(path: String): Config = Json.decodeFromString(File(path).readText())
    }
}