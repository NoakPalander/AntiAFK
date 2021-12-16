package com.antiafk.core

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

@OptIn(ExperimentalSerializationApi::class)
abstract class SnapshotSerializer<T: Any>(type: KClass<T>): KSerializer<SnapshotStateList<T>> {
    @OptIn(InternalSerializationApi::class)
    private val serializer = ListSerializer(type.serializer())

    override val descriptor: SerialDescriptor = SerialDescriptor("Snapshot", serializer.descriptor)

    override fun deserialize(decoder: Decoder): SnapshotStateList<T> {
        val deserialized = serializer.deserialize(decoder)
        return mutableStateListOf<T>().apply {
            deserialized.forEach(::add)
        }
    }

    override fun serialize(encoder: Encoder, value: SnapshotStateList<T>) {
        serializer.serialize(encoder, value)
    }
}