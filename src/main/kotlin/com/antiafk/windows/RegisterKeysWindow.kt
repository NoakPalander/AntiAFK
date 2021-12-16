package com.antiafk.windows

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
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
    val self = this

    var keyState by remember { mutableStateOf(TextFieldValue()) }
    var rawInput by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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
            if (!rawInput && it.type == KeyEventType.KeyDown && it.key != Key.Unknown) {
                keyState = TextFieldValue(
                    text = keyState.text + it.key.toString().substringAfter("Key: ").uppercase() + "\n"
                )
                scrollState.autoScroll(scope)
            }
            true
        }
    ) {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Text("Raw input")
                        Checkbox(checked = rawInput, onCheckedChange = {
                            rawInput = it
                        })

                        Text("Record presses")
                        Checkbox(checked = !rawInput, onCheckedChange = {
                            rawInput = !it
                        })
                    }

                    Box(modifier = Modifier.height(240.dp).fillMaxWidth()) {
                        TextField(
                            value = keyState,
                            readOnly = !rawInput,
                            onValueChange = {
                                keyState = TextFieldValue(it.text.uppercase(), selection = TextRange(it.selection.end))
                                scrollState.autoScroll(scope)
                            },
                            modifier = Modifier.background(Color.LightGray).fillMaxSize().verticalScroll(scrollState)
                        )
                        VerticalScrollbar(
                            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
                            adapter = rememberScrollbarAdapter(scrollState)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        // Add button
                        Button(modifier = Modifier.padding(5.dp), onClick = {
                            if (keyState.text.isNotEmpty())
                                state.loadedConfigName = ""

                            if (rawInput) {
                                // Converts the strings into single unique letter strings elements
                                state.keys.addAll(keyState.text.toSet().map { it.toString() })
                            }
                            else {
                                // Splits the text view and removes the leftover newline
                                state.keys.addAll(keyState.text.split("\n").let {
                                    it.take(it.size - 1)
                                }.toSet().map { it.uppercase() })
                            }

                            self.dispose(state)
                        }) {
                            Text("Done")
                        }
                        // Clear button
                        Button(modifier = Modifier.padding(5.dp), onClick = {
                            keyState = TextFieldValue()
                        }) {
                            Text("Clear")
                        }
                    }
                }
            }
        }
    }
}