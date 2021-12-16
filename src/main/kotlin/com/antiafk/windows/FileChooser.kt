package com.antiafk.windows

import java.io.File
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

fun openFileDialog(path: String): File? {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    return JFileChooser(path).let {
        it.dragEnabled = false
        it.isMultiSelectionEnabled = false
        it.dialogTitle = "Specify a config file to load."
        it.fileFilter = FileNameExtensionFilter("json file", "json")

        it.showOpenDialog(null)
        it.selectedFile
    }
}

fun saveFileDialog(path: String): File? {
    return JFileChooser(path).let {
        it.dragEnabled = false
        it.isMultiSelectionEnabled = false
        it.dialogTitle = "Specify a location to save the config file."
        it.fileFilter = FileNameExtensionFilter("json file", "json")

        it.showSaveDialog(null)
        if (it.selectedFile != null && !it.selectedFile.name.endsWith(".json"))
            it.selectedFile = File(it.selectedFile.absolutePath + ".json")

        it.selectedFile
    }
}