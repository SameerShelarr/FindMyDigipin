package com.sameershelar.findmydigipin.ui

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController(
    mapUIViewController: () -> UIViewController,
) = ComposeUIViewController {
    mapViewController = mapUIViewController
    App()
}

lateinit var mapViewController: () -> UIViewController
lateinit var mapActionCallback: (MapActionData) -> Unit