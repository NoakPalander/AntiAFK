package com.antiafk.core.serializer

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.*
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

object KeySerializer : SnapshotSerializer<Pair<Int, String>>(PairSerializer(Int.serializer(), String.serializer())) {
    override val descriptor = buildClassSerialDescriptor("Snapshot") {
        element<List<String>>("data")
    }
}

object ColorDeserializer : DeserializationStrategy<Color> {
    override val descriptor = serialDescriptor<String>()

    override fun deserialize(decoder: Decoder): Color {
        val decoded = decoder.decodeString().removePrefix("#")
        val (r, g, b) = when (decoded.length) {
            3 -> decoded.map { (it.toString() + it).toInt(16) }
            6 -> decoded.chunked(2).map { it.toInt(16) }
            else -> throw EncodingError("No valid encoding parsed, length was ${decoded.length}")
        }

        return Color(r, g, b)
    }
}

object PalletDeserializer : DeserializationStrategy<Colors> {
    private val delegateSerializer = JsonObject.serializer()
    override val descriptor = JsonObject.serializer().descriptor

    override fun deserialize(decoder: Decoder): Colors {
        val table = delegateSerializer.deserialize(decoder).map {
            it.key to Json.decodeFromString(ColorDeserializer, it.value.toString())
        }.toMap()

        return Colors(
            primary =           table["primary"]!!,
            primaryVariant =    table["primary_variant"]!!,
            secondary =         table["secondary"]!!,
            secondaryVariant =  table["secondary_variant"]!!,
            background =        table["background"]!!,
            surface =           table["surface"]!!,
            error =             table["error"]!!,
            onPrimary =         table["on_primary"]!!,
            onSecondary =       table["on_secondary"]!!,
            onBackground =      table["on_background"]!!,
            onSurface =         table["on_surface"]!!,
            onError =           table["on_error"]!!,
            isLight =           false
        )
    }
}