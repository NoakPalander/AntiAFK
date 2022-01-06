package com.antiafk.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.ApplicationScope
import com.antiafk.core.Config
import com.antiafk.core.serializer.PalletDeserializer
import com.antiafk.graphics.AppWindow
import com.antiafk.graphics.mainWindow
import com.antiafk.graphics.registerKeysWindow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.io.InputStream


@OptIn(ExperimentalSerializationApi::class)
@Composable
fun ApplicationScope.app() {
    // Reads the color pallet resource
    val pallet = String(useResource("/colors.json", InputStream::readAllBytes)).let { contents ->
        Json.decodeFromString(PalletDeserializer, contents)
    }

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
            active = true,
            onCompose = {
                registerKeysWindow(state)
            }
        )
    )

    windows.value.forEach { (_, window) ->
        window.compose(state)
    }
}
