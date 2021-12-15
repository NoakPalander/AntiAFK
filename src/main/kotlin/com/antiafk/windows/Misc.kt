package com.antiafk.windows

import java.io.File
import javax.swing.JFileChooser

fun openFileDialog(path: String): File {
    return JFileChooser(path).let {
        it.showOpenDialog(null)
        it.selectedFile
    }
}