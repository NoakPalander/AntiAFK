package com.antiafk.core

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element

object KeySerializer : SnapshotSerializer<String>(elementSerializer = String.serializer()) {
    override val descriptor = buildClassSerialDescriptor("Snapshot") {
        element<List<String>>("data")
    }
}