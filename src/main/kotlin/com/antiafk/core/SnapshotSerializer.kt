package com.antiafk.core

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

abstract class SnapshotSerializer<T>(elementSerializer: KSerializer<T>) : KSerializer<SnapshotStateList<T>> {
    private val delegateSerializer = ListSerializer(elementSerializer)

    override fun serialize(encoder: Encoder, value: SnapshotStateList<T>) {
        delegateSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): SnapshotStateList<T> {
        val deserialized = delegateSerializer.deserialize(decoder)
        return mutableStateListOf<T>().apply {
            deserialized.forEach(::add)
        }
    }
}
