package com.antiafk.app

import androidx.compose.runtime.*
import com.antiafk.windows.AppWindow

class AppState(val windows: MutableState<HashMap<String, AppWindow>>) {
    val keys = mutableStateListOf<String>()
}