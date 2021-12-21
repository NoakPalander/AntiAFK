package com.antiafk.core

import androidx.compose.material.Colors
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.File

data class Config(
    val pallet: Colors,
    val keys: SnapshotStateList<Pair<Int, String>>? = null,
    var saveFile: File? = null
)