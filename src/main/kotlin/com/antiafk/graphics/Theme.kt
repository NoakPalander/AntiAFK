package com.antiafk.graphics

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun darkTheme(pallet: Colors, content: @Composable () -> Unit) {
    MaterialTheme(colors = pallet) {
        content()
    }
}