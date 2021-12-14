package com.antiafk.core

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.application
import com.antiafk.windows.mainWindow
import com.antiafk.windows.registerKeysWindow

@ExperimentalComposeUiApi
fun main() = application {
    val mainWindowState = remember { mutableStateOf(false) }
    val keyWindowState = remember { mutableStateOf(true) }
    val keys = remember { mutableStateListOf<String>() }

    if (mainWindowState.value)
        mainWindow(keys, mainWindowState, keyWindowState)

    if (keyWindowState.value)
        registerKeysWindow(keys, keyWindowState)
    else
        exitApplication()

}