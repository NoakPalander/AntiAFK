package com.antiafk.windows

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

@Composable
fun keyItem(text: String, onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(text)
        Button(modifier = Modifier.padding(horizontal = 15.dp), onClick = onClick) {
            Text("-")
        }
    }
}

@Composable
fun keySection(keys: SnapshotStateList<String>) {
    val scrollState = rememberScrollState(0)

    Box(modifier = Modifier.size(300.dp, 350.dp).padding(horizontal = 20.dp).background(Color.LightGray)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            keys.forEach { key ->
                keyItem(key) {
                    keys.remove(key)
                }
            }
        }
        VerticalScrollbar(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}

@Composable
fun mainWindow(keys: SnapshotStateList<String>, mainState: MutableState<Boolean>, keyState: MutableState<Boolean>) {
    var textFieldState by remember { mutableStateOf(TextFieldValue()) }

    Window(
        title = "Anti AFK",
        onCloseRequest = { mainState.value = false },
        resizable = false,
        state = WindowState(size = WindowSize(800.dp, 700.dp))
    ) {
        DesktopMaterialTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray) {
                Column {
                    Column {
                        Row {
                            Text("Key pool", color = Color.White, modifier = Modifier.padding(22.dp, 10.dp))
                            Button(onClick = {
                                keyState.value = true
                            }) {
                                Text("+")
                            }
                        }
                        Row {
                            keySection(keys)
                        }
                    }

                    Text(
                        "Console output", color = Color.White,
                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp, start = 22.dp)
                    )
                    TextField(
                        value = textFieldState, readOnly = true, onValueChange = { textFieldState = it },
                        modifier = Modifier.fillMaxWidth().height(220.dp).background(Color.LightGray)
                    )
                }
            }
        }
    }
}