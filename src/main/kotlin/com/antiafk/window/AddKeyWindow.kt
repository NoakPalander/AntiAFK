package com.antiafk.window

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Window

@Composable
fun addKeyWindow(renderState: MutableState<Boolean>) {
    Window(
        onCloseRequest = { renderState.value = false }
    ) {
        Text("Hello!")
    }
}