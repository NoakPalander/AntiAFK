package com.antiafk.window

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState

@Composable
fun registerKeysWindow(keys: SnapshotStateList<String>, renderState: MutableState<Boolean>) {
    var keyState by remember { mutableStateOf(TextFieldValue()) }
    var rawInput by remember { mutableStateOf(false) }

    Window(
        title = "Register keys",
        resizable = false,
        state = WindowState(size = WindowSize(350.dp, 350.dp)),
        onCloseRequest = { renderState.value = false },
        onKeyEvent = {
            if (!rawInput && it.type == KeyEventType.KeyDown) {
                keyState = TextFieldValue(keyState.text + it.key.toString().substringAfter("Key: ") + "\n")
            }
            true
        }
    ) {
        DesktopMaterialTheme {
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

                    TextField(
                        value = keyState,
                        readOnly = !rawInput,
                        onValueChange = { keyState = it },
                        modifier = Modifier.fillMaxWidth().height(220.dp).background(Color.LightGray)
                    )
                    Row {
                        // Add button
                        Button(modifier = Modifier.padding(top = 10.dp), onClick = {
                            keys.addAll(keyState.text.split("\n").let {
                                it.take(it.size - 1)
                            }.toSet())

                            renderState.value = false
                        }) {
                            Text("Done")
                        }
                        // Clear button
                        Button(modifier = Modifier.padding(top = 10.dp), onClick = {
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