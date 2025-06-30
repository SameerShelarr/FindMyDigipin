package com.sameershelar.findmydigipin.ui

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Composable
expect fun MapComponent(
    onMapAction: MapAction,
)

fun interface MapAction {
    fun onAction(action: MapActionData)
}

@Serializable
sealed interface MapActionData {
    @Serializable
    data class DigiPin(val digiPin: String) : MapActionData
}
