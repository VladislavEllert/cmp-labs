package com.ellert.kriptan

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kriptan",
    ) {
        App()
    }
}