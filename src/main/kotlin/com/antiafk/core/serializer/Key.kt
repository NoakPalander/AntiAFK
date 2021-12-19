package com.antiafk.core.serializer

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.input.TextFieldValue

data class FieldPair(val field: TextFieldValue, val code: Key)
infix fun TextFieldValue.pair(code: Key) = FieldPair(this, code)

data class MutableFieldPair(var field: TextFieldValue, var code: Key)
infix fun TextFieldValue.mutablePair(code: Key) = MutableFieldPair(this, code)