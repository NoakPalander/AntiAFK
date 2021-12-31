package com.antiafk.graphics

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import com.antiafk.app.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


private fun ScrollState.autoScroll(scope: CoroutineScope) {
    scope.launch {
        scrollTo(maxValue)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppWindow.registerKeysWindow(state: AppState) {
    val scope = rememberCoroutineScope()
    val keys = mutableMapOf<Int, String>()
    val self = this

    var rawInput by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val console by remember { Console(editable = true) }

    Window(
        title = "Register keys",
        resizable = false,
        state = WindowState(
            size = DpSize(350.dp, 370.dp),
            position = state.windows.value["main"]!!.windowState.position.let {
                // Spawns the register window in the middle of the main window
                WindowPosition(it.x + 200.dp, it.y + 200.dp)
            }
        ).also {
           self.windowState = it
        },
        onCloseRequest = {
            self.dispose(state)
        },
        onKeyEvent = {
            // Key detection
            if (!rawInput && it.type == KeyEventType.KeyDown && it.key != Key.Unknown) {
                val representation = it.key.toString().substringAfter("Key: ").uppercase()
                keys[it.key.nativeKeyCode] = representation

                console.write("$representation\n".colored(Color.White))
                scrollState.autoScroll(scope)
            }
            true
        }
    ) {
        MaterialTheme(colors = state.config.pallet) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Raw input", color = MaterialTheme.colors.onPrimary)
                        Checkbox(
                            checked = rawInput,
                            onCheckedChange = { rawInput = it },
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = MaterialTheme.colors.onPrimary,
                                checkedColor = MaterialTheme.colors.primary,
                                uncheckedColor = MaterialTheme.colors.primary
                            )
                        )

                        Text("Record presses", color = MaterialTheme.colors.onPrimary)
                        Checkbox(
                            checked = !rawInput,
                            onCheckedChange = { rawInput = !it },
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = MaterialTheme.colors.onPrimary,
                                checkedColor = MaterialTheme.colors.primary,
                                uncheckedColor = MaterialTheme.colors.primary
                            )
                        )
                    }

                    console.compose(height = 240.dp, scrollState = scrollState)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        // Add button
                        Button(modifier = Modifier.padding(5.dp), onClick = {
                            if (console.text.isNotEmpty())
                                state.configLabel = ""

                            if (rawInput) {
                                // Converts the strings into single unique letter
                                state.keys.addAll(console.text.filter(Char::isLetter).map(Char::uppercaseChar).map {
                                    it.code to it.toString()
                                }.toSet())
                            }
                            else {
                                // Does the same as the above, but it's handled on-input instead of afterwards
                                state.keys.addAll(keys.toList())
                            }

                            // Disposes this window
                            self.dispose(state)
                        }) {
                            Text("Done")
                        }
                        // Clear button
                        Button(modifier = Modifier.padding(5.dp), onClick = {
                            console.clear()
                            keys.clear()
                        }) {
                            Text("Clear")
                        }
                    }
                }
            }
        }
    }
}