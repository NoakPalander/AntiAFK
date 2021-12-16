package com.antiafk.app

import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import com.antiafk.windows.AppWindow
import com.antiafk.windows.Console
import java.io.File

class AppState(val windows: MutableState<HashMap<String, AppWindow>>) {
    val keys = mutableStateListOf<String>()
    val console = Console()
    var config: File? = null
    var loadedConfigName by mutableStateOf("")
}