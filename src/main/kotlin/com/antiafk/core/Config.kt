package com.antiafk.core

import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: ...
@Serializable
data class Config(
    @SerialName("keys")
    @Serializable(with = KeySerializer::class)
    val keys: SnapshotStateList<String>,
)