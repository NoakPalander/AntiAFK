package com.antiafk.app

import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import com.antiafk.windows.AppWindow
import java.io.File

class AppState(val windows: MutableState<HashMap<String, AppWindow>>) {
    val keys = mutableStateListOf<String>()
    val console = mutableStateOf(TextFieldValue())
    var config: File? = null
}