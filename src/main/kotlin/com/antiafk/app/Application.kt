package com.antiafk.app

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ApplicationScope
import com.antiafk.windows.AppWindow
import com.antiafk.windows.mainWindow
import com.antiafk.windows.registerKeysWindow

@Composable
@ExperimentalMaterialApi
fun ApplicationScope.app() {
    val windows = remember { mutableStateOf(hashMapOf<String, AppWindow>()) }
    val state = AppState(windows)

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
        "key" to AppWindow(active = false, onCompose = {
            registerKeysWindow(state)
        })
    )

    windows.value.forEach { (_, window) ->
        window.compose(state)
    }
}