package com.antiafk.windows

import androidx.compose.runtime.*
import androidx.compose.ui.window.WindowState
import com.antiafk.app.AppState

class AppWindow(active: Boolean, private val onCompose: @Composable AppWindow.(AppState) -> Unit,
                private val onDispose: (AppState) -> Unit = {}) {

    var active by mutableStateOf(active)
    lateinit var windowState: WindowState

    @Composable
    fun compose(state: AppState) {
        if (active) {
            onCompose(state)
        }
    }

    fun dispose(state: AppState) {
        active = false
        onDispose(state)
    }
}