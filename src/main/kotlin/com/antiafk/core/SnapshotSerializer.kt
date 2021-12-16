package com.antiafk.core

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@OptIn(InternalSerializationApi::class)
internal inline fun <reified T: Any> serializer(): KSerializer<List<T>> = ListSerializer(T::class.serializer())

@OptIn(ExperimentalSerializationApi::class)
abstract class SnapshotSerializer <T>(private val serializer: KSerializer<List<T>>) : KSerializer<SnapshotStateList<T>> {
    override val descriptor: SerialDescriptor = SerialDescriptor("Snapshot", serializer.descriptor)

    override fun serialize(encoder: Encoder, value: SnapshotStateList<T>) {
        serializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): SnapshotStateList<T> {
        val deserialized = serializer.deserialize(decoder)
        return mutableStateListOf<T>().apply {
            deserialized.forEach {
                add(it)
            }
        }
    }
}