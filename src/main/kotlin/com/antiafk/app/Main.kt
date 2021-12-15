package com.antiafk.app

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.application
import com.antiafk.windows.mainWindow
import com.antiafk.windows.registerKeysWindow

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
fun main() = application {
    val mainWindowState = remember { mutableStateOf(true) }
    val keyWindowState = remember { mutableStateOf(false) }
    val keys = remember { mutableStateListOf<String>() }

    if (mainWindowState.value)
        mainWindow(keys, mainWindowState, keyWindowState)
    else
        exitApplication()

    if (keyWindowState.value)
        registerKeysWindow(keys, keyWindowState)
}