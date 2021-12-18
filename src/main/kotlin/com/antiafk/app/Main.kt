package com.antiafk.app

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.window.application
import kotlinx.serialization.ExperimentalSerializationApi
import java.awt.Robot

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalSerializationApi
fun main() = application {
    app()
}