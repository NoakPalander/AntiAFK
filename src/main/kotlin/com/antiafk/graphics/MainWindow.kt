package com.antiafk.graphics

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import com.antiafk.app.AppState
import com.antiafk.core.PostDelay
import com.antiafk.core.serializer.KeySerializer
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

@Composable
private fun keyItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = MaterialTheme.colors.onPrimary)
        Button(modifier = Modifier.padding(horizontal = 15.dp), onClick = onClick) {
            Text("-")
        }
    }
}

@Composable
private fun keySection(state: AppState) {
    val scrollState = rememberScrollState(0)

    Box(modifier = Modifier.size(300.dp, 388.dp).padding(horizontal = 20.dp).background(MaterialTheme.colors.onSurface)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            state.keys.forEach { key ->
                keyItem(key.second) {
                    state.keys.remove(key)
                    state.configLabel = ""
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(scrollState),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = MaterialTheme.colors.secondary,
                hoverColor = MaterialTheme.colors.secondaryVariant
            )
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun optionSection(state: AppState, scrollState: ScrollState) {
    var randomOrder by remember { mutableStateOf(true) }
    var randomDelay by remember { mutableStateOf(false) }
    
    var runState by remember { mutableStateOf(true) }
    var alertState by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column {
            Text("Keys", color = Color.White, modifier = Modifier.padding(bottom = 5.dp))
            Box(
                modifier = Modifier.size(280.dp, 50.dp).background(MaterialTheme.colors.onSurface),
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    // Add keys button
                    Button(onClick = {
                        // Enables the key window
                        state.windows.value["key"]!!.active = true
                    }) {
                        Text("Add")
                    }
                    // Remove button
                    Button(onClick = {
                        state.keys.clear()
                        state.configLabel = ""
                    }) {
                        Text("Clear")
                    }
                }
            }
        }
        Column {
            Row {
                Text("Config file", color = Color.White, modifier = Modifier.padding(top = 20.dp, bottom = 5.dp))
                Text(
                    text = state.configLabel,
                    color = Color.LightGray,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(top = 20.dp, bottom = 5.dp)
                )
            }
            Box(
                modifier = Modifier.size(280.dp, 50.dp).background(MaterialTheme.colors.onSurface),
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    // Save button
                    Button(onClick = {
                        state.config.saveFile = saveFileDialog(System.getProperty("user.home"))
                        if (state.config.saveFile != null) {
                            File(state.config.saveFile!!.absolutePath).writeText(
                                Json.encodeToString(
                                    KeySerializer,
                                    state.keys
                                )
                            )
                            state.console.log("Saved config '${state.config.saveFile!!.name}'".colored(Color.Green))
                        }
                    }) {
                        Text("Save")
                    }
                    // Load button
                    Button(onClick = {
                        try {
                            state.config.saveFile = openFileDialog(System.getProperty("user.home"))
                            if (state.config.saveFile != null) {
                                state.configLabel = "| ${state.config.saveFile!!.name}"

                                val contents = File(state.config.saveFile!!.absolutePath).readText()
                                state.keys.clear()
                                state.keys.addAll(Json.decodeFromString(KeySerializer, contents))
                                state.console.log("Loaded config '${state.config.saveFile!!.name}'\n".colored(Color.Green))
                            }
                        } catch (e: FileNotFoundException) {
                            println(e)
                        }
                    }) {
                        Text("Load")
                    }
                }
            }
        }
        Column {
            Text("Options", color = Color.White, modifier = Modifier.padding(top = 20.dp, bottom = 5.dp))
            Box(
                modifier = Modifier.width(320.dp).background(MaterialTheme.colors.onSurface),
                contentAlignment = Alignment.Center
            ) {
                var delayText by remember { mutableStateOf("Delay time: 200ms") }
                var delayRange by remember { mutableStateOf(100f..1000f) }
                val updateDelayText = {
                    delayText = "Delay time: ${delayRange.start.toInt()}ms to ${delayRange.endInclusive.toInt()}ms"
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Text(
                            text = "Rand. order",
                            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp, start = 15.dp),
                            color = MaterialTheme.colors.onPrimary
                        )
                        Checkbox(
                            checked = randomOrder,
                            onCheckedChange = { randomOrder = !randomOrder },
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = MaterialTheme.colors.onPrimary,
                                checkedColor = MaterialTheme.colors.primary,
                                uncheckedColor = MaterialTheme.colors.primary
                            )
                        )

                        Text(
                            text = "Rand. delay",
                            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp, start = 15.dp),
                            color = MaterialTheme.colors.onPrimary
                        )
                        Checkbox(
                            checked = randomDelay,
                            onCheckedChange = {
                                randomDelay = !randomDelay
                                if (randomDelay)
                                    updateDelayText()
                                else
                                    delayText = "Delay time: 200ms"
                            },
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = MaterialTheme.colors.onPrimary,
                                checkedColor = MaterialTheme.colors.primary,
                                uncheckedColor = MaterialTheme.colors.primary
                            )
                        )
                    }

                    Text(delayText, modifier = Modifier.align(Alignment.Start).padding(start = 18.dp))

                    RangeSlider(
                        enabled = randomDelay,
                        values = delayRange,
                        valueRange = 100f..1000f,
                        steps = 8,
                        onValueChange = { delayRange = it },
                        onValueChangeFinished = { updateDelayText() }
                    )

                    // Run/Stop button
                    Button(onClick = {
                        if (state.keys.isEmpty()) {
                            alertState = true
                        } else {
                            if (runState) {
                                val postDelay = if (randomDelay) {
                                    PostDelay.Interval(delayRange.start.toLong(), delayRange.endInclusive.toLong())
                                } else {
                                    PostDelay.Default(200L)
                                }

                                scope.launch {
                                    state.simulator.run(
                                        keys = state.keys.toTypedArray(),
                                        shuffle = randomOrder,
                                        postDelay = postDelay,
                                        onRelease = {
                                            state.console.log("'${it}' was pressed.\n".colored(Color.Black))
                                            scrollState.scrollTo(scrollState.maxValue)
                                        }
                                    )
                                }
                            } else {
                                scope.launch {
                                    state.simulator.stop()
                                }
                            }

                            runState = !runState
                        }
                    }) {
                        Text(if (runState) "Run" else "Stop")
                    }
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

@Composable
fun AppWindow.mainWindow(state: AppState) {
    val self = this
    val scrollState = rememberScrollState()

    Window(
        title = "AntiAFK",
        onCloseRequest = { self.dispose(state) },
        resizable = false,
        state = WindowState(size = DpSize(800.dp, 750.dp)).also {
            self.windowState = it
        }
    ) {
        MaterialTheme(colors = state.config.pallet) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column {
                    Column {
                        Text(
                            text = "Key pool",
                            modifier = Modifier.padding(22.dp, 10.dp),
                            color = MaterialTheme.colors.onPrimary
                        )
                        Row {
                            keySection(state)
                            optionSection(state, scrollState)
                        }
                    }
                    Text(
                        text = "Console output",
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp, start = 22.dp)
                    )
                    state.console.compose(height = 240.dp, scrollState = scrollState)
                }
            }
        }
    }
}