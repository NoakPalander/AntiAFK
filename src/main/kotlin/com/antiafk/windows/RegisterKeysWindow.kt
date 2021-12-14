package com.antiafk.windows

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private fun ScrollState.autoScroll() {
    CoroutineScope(Dispatchers.IO).launch {
        scrollTo(maxValue)
    }
}

@Composable
fun registerKeysWindow(keys: SnapshotStateList<String>, renderState: MutableState<Boolean>) {
    var keyState by remember { mutableStateOf(TextFieldValue()) }
    var rawInput by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Window(
        title = "Register keys",
        resizable = false,
        state = WindowState(size = WindowSize(350.dp, 350.dp)),
        onCloseRequest = { renderState.value = false },
        onKeyEvent = {
            if (!rawInput && it.type == KeyEventType.KeyDown) {
                keyState = TextFieldValue(keyState.text + it.key.toString().substringAfter("Key: ")
                    .uppercase() + "\n")

                scrollState.autoScroll()
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

                    Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                        TextField(
                            value = keyState,
                            readOnly = !rawInput,
                            onValueChange = {
                                keyState = TextFieldValue(it.text.uppercase(), selection = TextRange(it.selection.end))
                                scrollState.autoScroll()
                            },
                            modifier = Modifier.background(Color.LightGray).fillMaxSize().verticalScroll(scrollState)
                        )
                        VerticalScrollbar(
                            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
                            adapter = rememberScrollbarAdapter(scrollState)
                        )
                    }


                    Row {
                        // Add button
                        Button(modifier = Modifier.padding(top = 10.dp), onClick = {
                            if (rawInput) {
                                keys.addAll(keyState.text.toSet().map { it.toString() })
                            }
                            else {
                                keys.addAll(keyState.text.split("\n").let {
                                    it.take(it.size - 1)
                                }.toSet().map { it.uppercase() })
                            }

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