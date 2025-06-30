package com.sameershelar.findmydigipin.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController

@Composable
actual fun MapComponent(
    onMapAction: MapAction,
) {
    UIKitViewController(
        factory = mapViewController,
        modifier = Modifier.fillMaxSize(),
    )
    mapActionCallback = { action ->
        onMapAction.onAction(
            action = action
        )
    }
}
