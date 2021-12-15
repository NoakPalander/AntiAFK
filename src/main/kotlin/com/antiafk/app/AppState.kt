package com.antiafk.app

import androidx.compose.runtime.*
import com.antiafk.windows.AppWindow
import java.io.File

class AppState(val windows: MutableState<HashMap<String, AppWindow>>) {
    val keys = mutableStateListOf<String>()
    var config: File? = null
}