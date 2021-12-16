package com.antiafk.core

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@ExperimentalSerializationApi
class KeySerializer : KSerializer<SnapshotStateList<String>> {
    private val delegateSerializer = ListSerializer(String.serializer())
    override val descriptor: SerialDescriptor = SerialDescriptor("Keys", delegateSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: SnapshotStateList<String>) {
        delegateSerializer.serialize(encoder, value.toList())
    }

    override fun deserialize(decoder: Decoder): SnapshotStateList<String> {
        val deserialized = delegateSerializer.deserialize(decoder)
        return mutableStateListOf(*deserialized.toTypedArray())
    }
}