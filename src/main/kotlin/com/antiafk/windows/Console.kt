package com.antiafk.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.datetime.LocalDateTime

fun String.colored(color: Color): AnnotatedString {
    return AnnotatedString.Builder().apply {
        withStyle(style = SpanStyle(color)) {
            append(this@colored)
        }
    }.toAnnotatedString()
}

class Console {
    private var buffer by mutableStateOf(TextFieldValue())
    private val transformation = VisualTransformation { text -> TransformedText(text, OffsetMapping.Identity) }

    fun clear() {
        buffer = TextFieldValue()
    }

    // Logs color formatted strings with timestamps
    fun log(vararg str: AnnotatedString) {
        // TODO: Add local date time
        val newText = AnnotatedString.Builder().apply {
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
    fun compose(background: Color) {
        Text(
            text = "Console output",
            color = Color.White,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp, start = 22.dp)
        )
        TextField(
            value = buffer,
            readOnly = true,
            onValueChange = { buffer = it },
            modifier = Modifier.fillMaxWidth().height(220.dp).background(background),
            visualTransformation = transformation
        )
    }
}