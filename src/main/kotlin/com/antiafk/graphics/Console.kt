package com.antiafk.graphics

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.reflect.KProperty

fun String.colored(color: Color): AnnotatedString {
    return AnnotatedString.Builder().apply {
        withStyle(style = SpanStyle(color)) {
            append(this@colored)
        }
    }.toAnnotatedString()
}

class Console(private val editable: Boolean = false) {
    private val transformation = VisualTransformation { text -> TransformedText(text, OffsetMapping.Identity) }
    private var buffer by mutableStateOf(TextFieldValue())
    val text: String
        get() = buffer.text


    fun clear() {
        buffer = TextFieldValue()
    }

    // Logs color formatted strings with timestamps
    fun log(vararg str: AnnotatedString) {
        val dt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).let {
            "[${it.dayOfMonth}/${it.monthNumber}-${it.year}, ${it.hour}:${it.minute}:${it.second}]:"
        }

        val newText = AnnotatedString.Builder().apply {
            append("$dt ".colored(Color.Black))
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

    // Raw annotated strings
    fun write(vararg str: AnnotatedString) {
        val newText = AnnotatedString.Builder().apply {
            str.forEach(::append)
        }.toAnnotatedString()

        buffer = TextFieldValue(buffer.annotatedString + transformation.filter(newText).text)
    }

    @Composable
    fun compose(height: Dp, scrollState: ScrollState) {
        Box(modifier = Modifier.fillMaxWidth().height(height)) {
            TextField(
                value = buffer,
                readOnly = !editable,
                onValueChange = {
                    buffer = if (editable) {
                        clear()
                        write(it.text.colored(Color.White))
                        TextFieldValue(
                            transformation.filter(buffer.annotatedString).text,
                            TextRange(it.selection.end)
                        )
                    }
                    else {
                        it
                    }
                },
                modifier = Modifier.fillMaxWidth().height(height).background(MaterialTheme.colors.onSurface)
                    .verticalScroll(scrollState),
                visualTransformation = transformation
            )
            VerticalScrollbar(
                modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }
    }
}

// for 'by remember' delegate
operator fun Console.getValue(thisRef: Any?, property: KProperty<*>): Console = this