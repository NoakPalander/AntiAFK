package com.antiafk.windows

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import com.antiafk.app.AppState
import com.antiafk.core.KeySerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

@Composable
private fun keyItem(text: String, onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(text)
        Button(modifier = Modifier.padding(horizontal = 15.dp), onClick = onClick) {
            Text("-")
        }
    }
}

@Composable
private fun keySection(state: AppState) {
    val scrollState = rememberScrollState(0)

    Box(modifier = Modifier.size(300.dp, 350.dp).padding(horizontal = 20.dp).background(Color.LightGray)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            state.keys.forEach { key ->
                keyItem(key) {
                    state.keys.remove(key)
                }
            }
        }
        VerticalScrollbar(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalSerializationApi::class)
@Composable
private fun optionSection(state: AppState) {
    var random by remember { mutableStateOf(true) }
    var runState by remember { mutableStateOf(true) }
    var alertState by remember { mutableStateOf(false) }

    Column {
        Text("Add/Clear available keys", color = Color.White, modifier = Modifier.padding(bottom = 5.dp))
        Box(modifier = Modifier.background(Color.LightGray).size(255.dp, 50.dp), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Add keys button
                Button(onClick = {
                    // Enables the key window
                    state.windows.value["key"]!!.active = true
                }) {
                    Text("Add keys")
                }
                // Remove button
                Button(onClick = {
                    state.keys.clear()
                    state.loadedConfigName = ""
                }) {
                    Text("Remove all")
                }
            }
        }

        Row {
            Text("Config file", color = Color.White, modifier = Modifier.padding(top = 20.dp, bottom = 5.dp))
            Text(
                text = state.loadedConfigName,
                color = Color.LightGray,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 20.dp, bottom = 5.dp)
            )
        }
        Box(modifier = Modifier.background(Color.LightGray).size(255.dp, 50.dp), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Save button
                Button(onClick = {
                    state.config = saveFileDialog(System.getProperty("user.home"))
                    if (state.config != null) {
                        File(state.config!!.absolutePath).writeText(Json.encodeToString(KeySerializer, state.keys))
                        println(state.config)
                    }
                }) {
                    Text("Save")
                }
                // Load button
                Button(onClick = {
                    try {
                        state.config = openFileDialog(System.getProperty("user.home"))
                        if (state.config != null) {
                            state.loadedConfigName = "| ${state.config!!.name}"

                            val contents = File(state.config!!.absolutePath).readText()
                            state.keys.clear()
                            state.keys.addAll(Json.decodeFromString(KeySerializer, contents))

                            state.console.log("Hello\n".colored(Color.Black),
                                "Loaded ${state.config!!.name}\n".colored(Color.Red))
                        }
                    }
                    catch(e: FileNotFoundException) {
                        println(e)
                    }
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
                    if (state.keys.isEmpty()) {
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
                    Text("OK")
                }
            },
            text = { Text("No keys to press were found, please either add them or load a premade config file.") },
            title = { Text("No keys found!", fontWeight = FontWeight.Bold) }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalSerializationApi::class)
@Composable
fun AppWindow.mainWindow(state: AppState) {
    val self = this

    Window(
        title = "AntiAFK",
        onCloseRequest = { self.dispose(state) },
        resizable = false,
        state = WindowState(size = DpSize(800.dp, 700.dp)).also {
            self.windowState = it
        }
    ) {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray) {
                Column {
                    Column {
                        Text("Key pool", color = Color.White, modifier = Modifier.padding(22.dp, 10.dp))
                        Row {
                            keySection(state)
                            optionSection(state)
                        }
                    }

                    state.console.compose(Color.LightGray)
                }
            }
        }
    }
}