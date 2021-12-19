package com.antiafk.core.serializer

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import com.antiafk.core.SnapshotSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object KeySerializer : SnapshotSerializer<Pair<Int, String>>(PairSerializer(Int.serializer(), String.serializer())) {
    override val descriptor = buildClassSerialDescriptor("Snapshot") {
        element<List<String>>("data")
    }
}

object ColorSerializer : KSerializer<Color> {
    override val descriptor = buildClassSerialDescriptor("Color") {

    }

    override fun deserialize(decoder: Decoder): Color {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: Color) {
        TODO("Not yet implemented")
    }

}

object PalleteSerializer : KSerializer<Colors> {
    override val descriptor = buildClassSerialDescriptor("Pallete") {
        element<Color>("primary")
        element<Color>("primary_variant")
        element<Color>("secondary")
        element<Color>("secondary_variant")
        element<Color>("background")
        element<Color>("surface")
        element<Color>("error")
        element<Color>("on_primary")
        element<Color>("on_secondary")
        element<Color>("on_background")
        element<Color>("on_surface")
        element<Color>("on_error")
        element<Boolean>("is_light")
    }

    override fun deserialize(decoder: Decoder): Colors {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: Colors) {
        TODO("Not yet implemented")
    }
}