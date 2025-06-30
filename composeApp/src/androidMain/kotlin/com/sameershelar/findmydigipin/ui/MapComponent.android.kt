package com.sameershelar.findmydigipin.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.sameershelar.findmydigipin.domain.DigiPinUsecase
import com.sameershelar.findmydigipin.utils.onGoToMyLocationClick
import com.sameershelar.findmydigipin.utils.onSearchClick
import com.sameershelar.findmydigipin.utils.onShareDigipinClick
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun MapComponent(
    onMapAction: MapAction,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        val locationPermissionState =
            rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
        val coordinates = LatLng(23.0, 78.0)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(coordinates, 5f)
        }
        var marker by remember { mutableStateOf<LatLng?>(null) }
        val scope = rememberCoroutineScope()

        suspend fun setMarkerAndAnimateCamera(latitude: Double, longitude: Double) {
            val searchedCoordinates = LatLng(latitude, longitude)
            marker = searchedCoordinates
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(searchedCoordinates, 17f),
            )
            val digipin = DigiPinUsecase.getDigiPin(
                lat = searchedCoordinates.latitude,
                lon = searchedCoordinates.longitude,
            )

            onMapAction.onAction(
                MapActionData.DigiPin(digipin)
            )
        }

        @SuppressLint("MissingPermission")
        suspend fun goToMyLocation() {
            if (locationPermissionState.status == PermissionStatus.Granted) {
                marker = null
                val lastLocation = suspendCoroutine<Location?> { continuation ->
                    fusedClient.lastLocation.addOnSuccessListener { location ->
                        continuation.resume(location)
                    }.addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
                }

                lastLocation?.let { location ->
                    val userCoordinates = LatLng(location.latitude, location.longitude)
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(userCoordinates, 17f),
                    )

                    val digipin = DigiPinUsecase.getDigiPin(
                        lat = location.latitude,
                        lon = location.longitude,
                    )

                    onMapAction.onAction(
                        MapActionData.DigiPin(digipin)
                    )
                }
            }
        }

        LaunchedEffect(Unit) {
            if (locationPermissionState.status != PermissionStatus.Granted) {
                locationPermissionState.launchPermissionRequest()
            }

            onSearchClick = { latitude, longitude ->
                scope.launch {
                    setMarkerAndAnimateCamera(latitude, longitude)
                }
            }

            onShareDigipinClick = { digiPin ->
                val (lat, long) = DigiPinUsecase.getLatLngByDigipin(digiPin)
                val shareMessage = """
                    DigiPin: $digiPin
                    
                    Maps Link: https://www.google.com/maps/search/?api=1&query=$lat,$long"
                """.trimIndent()
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share DigiPin via"))
            }

            onGoToMyLocationClick = {
                scope.launch {
                    goToMyLocation()
                }
            }
        }

        // once permission is granted, fetch last location and animate camera
        LaunchedEffect(locationPermissionState.status) {
            goToMyLocation()
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = locationPermissionState.status == PermissionStatus.Granted,
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
            ),
            onMapLongClick = { latLng ->
                println("Long pressed at: ${latLng.latitude}, ${latLng.longitude}")
                marker = latLng

                val digipin = DigiPinUsecase.getDigiPin(
                    lat = latLng.latitude,
                    lon = latLng.longitude,
                )

                println("DigiPin: $digipin")

                onMapAction.onAction(
                    MapActionData.DigiPin(digipin)
                )
            },
        ) {
            marker?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Selected DigiPin Location",
                    snippet = "Lat: ${it.latitude}, Lng: ${it.longitude}",
                )
            }
        }
    }
}