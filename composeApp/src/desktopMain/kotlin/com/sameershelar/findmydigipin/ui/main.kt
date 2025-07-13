package com.sameershelar.findmydigipin.ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Find My Digipin",
    ) {
        App()
    }
}