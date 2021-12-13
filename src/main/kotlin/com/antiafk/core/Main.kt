package com.antiafk.core

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.application
import com.antiafk.window.addKeyWindow
import com.antiafk.window.mainWindow

@ExperimentalComposeUiApi
fun main() = application {
    val mainWindowState = remember { mutableStateOf(true) }
    val keyWindowState = remember { mutableStateOf(false) }

    if (mainWindowState.value)
        mainWindow(mainWindowState, keyWindowState)
    else
        exitApplication()

    if (keyWindowState.value)
        addKeyWindow(keyWindowState)
}