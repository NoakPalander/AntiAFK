package com.antiafk.graphics

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.colored(color: Color): AnnotatedString {
    return AnnotatedString.Builder().apply {
        withStyle(style = SpanStyle(color)) {
            append(this@colored)
        }
    }.toAnnotatedString()
}

class Console {
    private val transformation = VisualTransformation { text -> TransformedText(text, OffsetMapping.Identity) }
    private var buffer by mutableStateOf(TextFieldValue())

    private val datetime
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).let {
            "[${it.dayOfMonth}/${it.monthNumber}-${it.year}, ${it.hour}:${it.minute}:${it.second}]:"
        }

    fun clear() {
        buffer = TextFieldValue()
    }

    // Logs color formatted strings with timestamps
    fun log(vararg str: AnnotatedString) {
        val newText = AnnotatedString.Builder().apply {
            append("$datetime ".colored(Color.Blue))
            str.forEach(::append)
        }.toAnnotatedString()

        buffer = TextFieldValue(buffer.annotatedString + transformation.filter(newText).text)
    }

    // Raw strings
    fun write(vararg str: String) {
        val newText = AnnotatedString.Builder().apply {
            str.forEach(::append)
        }.toAnnotatedString()

        buffer = TextFieldValue(buffer.annotatedString + newText)
    }

    @Composable
    fun compose(background: Color, scrollState: ScrollState) {
        Text(
            text = "Console output",
            color = Color.White,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp, start = 22.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = buffer,
                readOnly = false,
                onValueChange = { buffer = it },
                modifier = Modifier.fillMaxWidth().height(220.dp).background(background).verticalScroll(scrollState),
                visualTransformation = transformation
            )
            VerticalScrollbar(
                modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }
    }
}