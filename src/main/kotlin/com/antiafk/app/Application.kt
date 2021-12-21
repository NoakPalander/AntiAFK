package com.antiafk.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.ApplicationScope
import com.antiafk.core.Config
import com.antiafk.core.serializer.PalletSerializer
import com.antiafk.graphics.AppWindow
import com.antiafk.graphics.mainWindow
import com.antiafk.graphics.registerKeysWindow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


@OptIn(ExperimentalSerializationApi::class)
@Composable
fun ApplicationScope.app() {
    // Reads the color pallet resource
    val pallet = Json.decodeFromString(PalletSerializer, String(useResource("/colors.json") {
        it.readAllBytes()
    }))

    val config = Config(pallet)
    val windows = remember { mutableStateOf(hashMapOf<String, AppWindow>()) }
    val state = AppState(windows, config)

    windows.value = hashMapOf(
        "main" to AppWindow(
            active = true,
            onCompose = {
                mainWindow(state)
            },
            onDispose = {
                exitApplication()
            }
        ),
        "key" to AppWindow(
            active = false,
            onCompose = {
                registerKeysWindow(state)
            }
        )
    )

    windows.value.forEach { (_, window) ->
        window.compose(state)
    }
}
