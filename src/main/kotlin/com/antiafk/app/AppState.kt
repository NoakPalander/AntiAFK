package com.antiafk.app

import androidx.compose.runtime.*
import com.antiafk.core.Config
import com.antiafk.core.Simulator
import com.antiafk.graphics.AppWindow
import com.antiafk.graphics.Console

class AppState(val windows: MutableState<HashMap<String, AppWindow>>, val config: Config) {
    val keys = mutableStateListOf<Pair<Int, String>>()
    val simulator = Simulator()
    val console = Console()
    var configLabel by mutableStateOf("")
}