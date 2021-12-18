package com.antiafk.app

import androidx.compose.runtime.*
import com.antiafk.core.Simulator
import com.antiafk.windows.AppWindow
import com.antiafk.windows.Console
import java.io.File

class AppState(val windows: MutableState<HashMap<String, AppWindow>>) {
    val keys = mutableStateListOf<Pair<Int, String>>()
    val simulator = Simulator()
    val console = Console()
    var config: File? = null
    var loadedConfigName by mutableStateOf("")
}