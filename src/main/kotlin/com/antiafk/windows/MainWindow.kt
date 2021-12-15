package com.antiafk.windows

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import java.awt.FileDialog
import java.io.File
import javax.swing.JFileChooser

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
@ExperimentalMaterialApi
fun optionSection(window: ComposeWindow, keys: SnapshotStateList<String>, keyState: MutableState<Boolean>) {
    var random by remember { mutableStateOf(true) }
    var runState by remember { mutableStateOf(true) }
    var alertState by remember { mutableStateOf(false) }

    Column {
        Text("Add/Clear available keys", color = Color.White, modifier = Modifier.padding(bottom = 5.dp))
        Box(modifier = Modifier.background(Color.LightGray).size(255.dp, 50.dp), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Add keys button
                Button(onClick = {
                    keyState.value = true
                }) {
                    Text("Add keys")
                }
                // Remove button
                Button(onClick = {
                    keys.clear()
                }) {
                    Text("Remove all")
                }
            }
        }

        Text("Config file", color = Color.White, modifier = Modifier.padding(top = 20.dp, bottom = 5.dp))
        Box(modifier = Modifier.background(Color.LightGray).size(255.dp, 50.dp), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Save button
                Button(onClick = {

                }) {
                    Text("Save")
                }
                // Load button
                Button(onClick = {
                    val file = openFileDialog(System.getProperty("user.home"))
                    
                    println(file)
                }) {
                    Text("Load")
                }
            }
        }

        Text("Poll", color = Color.White, modifier = Modifier.padding(top = 20.dp, bottom = 5.dp))
        Box(modifier = Modifier.background(Color.LightGray).size(255.dp, 50.dp), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Run/Stop button
                Button(onClick = {
                    if (keys.isEmpty()) {
                        alertState = true
                    }
                    else {
                        if (runState) {
                            // Run..
                        } else {
                            // Stop..
                        }

                        runState = !runState
                    }
                }) {
                    Text(if (runState) "Run" else "Stop")
                }

                // Random order / in order
                Button(onClick = { random = !random }) {
                    Text(if (random) "Random" else "In order")
                }
            }
        }
    }

    if (alertState) {
        AlertDialog(
            modifier = Modifier.size(width = 200.dp, height = 200.dp),
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    alertState = false
                }) {
                    Text("Ok")
                }
            },
            text = { Text("No keys to press were found, please either add them or load a premade config file.") },
            title = { Text("No keys found!", fontWeight = FontWeight.Bold) }
        )
    }
}

@Composable
@ExperimentalMaterialApi
fun mainWindow(keys: SnapshotStateList<String>, mainState: MutableState<Boolean>, keyState: MutableState<Boolean>) {
    var textFieldState by remember { mutableStateOf(TextFieldValue()) }

    Window(
        title = "Anti AFK",
        onCloseRequest = { mainState.value = false },
        resizable = false,
        state = WindowState(size = DpSize(800.dp, 700.dp))
    ) {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray) {
                Column {
                    Column {
                        Text("Key pool", color = Color.White, modifier = Modifier.padding(22.dp, 10.dp))
                        Row {
                            keySection(keys)
                            optionSection(window, keys, keyState)
                        }
                    }

                    Text(
                        text = "Console output",
                        color = Color.White,
                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp, start = 22.dp)
                    )
                    TextField(
                        value = textFieldState,
                        readOnly = true,
                        onValueChange = { textFieldState = it },
                        modifier = Modifier.fillMaxWidth().height(220.dp).background(Color.LightGray)
                    )
                }
            }
        }
    }
}