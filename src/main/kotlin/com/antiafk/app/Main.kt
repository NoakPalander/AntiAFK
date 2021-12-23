package com.antiafk.app

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.application
import kotlinx.serialization.ExperimentalSerializationApi


// TODO: Size-limit the console output
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalSerializationApi
fun main() = application {
    app()
}