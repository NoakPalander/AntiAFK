package com.antiafk.core.serializer

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.antiafk.core.SnapshotSerializer
import kotlinx.serialization.*
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull.serializer
import kotlinx.serialization.json.JsonObject

object KeySerializer : SnapshotSerializer<Pair<Int, String>>(PairSerializer(Int.serializer(), String.serializer())) {
    override val descriptor = buildClassSerialDescriptor("Snapshot") {
        element<List<String>>("data")
    }
}

object ColorSerializer : DeserializationStrategy<Color> {
    override val descriptor = serialDescriptor<String>()

    override fun deserialize(decoder: Decoder): Color {
        val decoded = decoder.decodeString()
        return Color(
            red = decoded.substring(0, 2).toInt(16),
            green = decoded.substring(2, 4).toInt(16),
            blue = decoded.substring(4).toInt(16),
        )
    }
}

object PalletSerializer : DeserializationStrategy<Colors> {
    private val delegateSerializer = JsonObject.serializer()
    override val descriptor = JsonObject.serializer().descriptor

    override fun deserialize(decoder: Decoder): Colors {
        val table = delegateSerializer.deserialize(decoder).map {
            it.key to Json.decodeFromString(ColorSerializer, it.value.toString())
        }.toMap()

        return Colors(
            primary = table["primary"]!!,
            primaryVariant = table["primary_variant"]!!,
            secondary = table["secondary"]!!,
            secondaryVariant = table["secondary_variant"]!!,
            background = table["background"]!!,
            surface = table["surface"]!!,
            error = table["error"]!!,
            onPrimary = table["on_primary"]!!,
            onSecondary = table["on_secondary"]!!,
            onBackground = table["on_background"]!!,
            onSurface = table["on_surface"]!!,
            onError = table["on_error"]!!,
            isLight = false
        )
    }
}