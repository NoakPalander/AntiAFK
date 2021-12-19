package com.antiafk.app

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.window.application
import kotlinx.serialization.ExperimentalSerializationApi
import java.awt.Robot
import java.io.File

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalSerializationApi
fun main() = application {
    println(File(System.getProperty("compose.application.resources.dir") + "/config.json").readText())
//app()
}