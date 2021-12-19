package com.antiafk.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ApplicationScope
import com.antiafk.core.Config
import com.antiafk.graphics.AppWindow
import com.antiafk.graphics.mainWindow
import com.antiafk.graphics.registerKeysWindow

@Composable
fun ApplicationScope.app() {
    val config = Config.load({}.javaClass.getResource("/config.json").path)
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
