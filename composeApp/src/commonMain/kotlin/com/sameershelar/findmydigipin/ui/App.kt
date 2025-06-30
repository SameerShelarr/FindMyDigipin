package com.sameershelar.findmydigipin.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.russhwolf.settings.Settings
import com.sameershelar.findmydigipin.domain.DigiPinUsecase
import com.sameershelar.findmydigipin.utils.CONSTANTS.IS_USER_GUIDE_SHOWN_KEY
import com.sameershelar.findmydigipin.utils.ShareIcon
import com.sameershelar.findmydigipin.utils.clipEntryOf
import com.sameershelar.findmydigipin.utils.onGoToMyLocationClick
import com.sameershelar.findmydigipin.utils.onSearchClick
import com.sameershelar.findmydigipin.utils.onShareDigipinClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            var digiPin by remember { mutableStateOf("") }
            val clipboard = LocalClipboard.current
            val scope = rememberCoroutineScope()

            var isSearchMode by remember { mutableStateOf(false) }
            var isDigiPinSearchMode by remember { mutableStateOf(false) }
            var latitude by remember { mutableStateOf("") }
            var longitude by remember { mutableStateOf("") }
            var digiPinSearch by remember { mutableStateOf("") }

            // State to control the dialog visibility
            var showDialog by remember { mutableStateOf(false) }

            // Show the usage guide dialog
            UserGuideDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false }
            )

            LaunchedEffect(null) {
                val settings = Settings()

                val isUserGuideShown = settings.getBoolean(
                    key = IS_USER_GUIDE_SHOWN_KEY,
                    defaultValue = false
                )

                if (isUserGuideShown) return@LaunchedEffect

                delay(10_000)
                showDialog = true

                // Set the flag to true after showing the dialog
                settings.putBoolean(
                    key = IS_USER_GUIDE_SHOWN_KEY,
                    value = true
                )
            }

            MapComponent(
                onMapAction = {
                    when (it) {
                        is MapActionData.DigiPin -> {
                            println("Received DigiPin: ${it.digiPin}")
                            digiPin = it.digiPin
                        }
                    }
                }
            )

            // Combine the card, search button, and text fields into a Column
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 52.dp
                    )
                    .imePadding(), // Add padding when the keyboard is visible
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                // Icon for user guide
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            if (showDialog.not()) {
                                showDialog = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QuestionMark,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Icon for current location
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            // go to current location
                            onGoToMyLocationClick?.invoke()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                // DigiPin card and search button
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Card with DigiPin text
                    if (digiPin.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(76.dp)
                                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(
                                    text = digiPin,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray,
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically)
                                )
                                Icon(
                                    imageVector = ShareIcon,
                                    contentDescription = "Share DigiPin",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(bounded = false)
                                        ) {
                                            val digiPinToShare = digiPin
                                            onShareDigipinClick?.invoke(digiPinToShare)
                                        }
                                )
                                Spacer(
                                    modifier = Modifier.size(10.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy DigiPin",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(bounded = false)
                                        ) {
                                            scope.launch {
                                                clipboard.setClipEntry(
                                                    clipEntryOf(digiPin)
                                                )
                                            }
                                        }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(12.dp))

                    // Search button
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                if (isSearchMode) {
                                    // Perform search
                                    if (isDigiPinSearchMode.not()) {
                                        println("Searching with Lat: $latitude, Lon: $longitude")
                                        val lat = latitude.toDoubleOrNull()
                                        val lon = longitude.toDoubleOrNull()
                                        if (lat != null && lon != null) {
                                            onSearchClick?.invoke(lat, lon)
                                        } else {
                                            println("Invalid latitude or longitude for search")
                                        }
                                    } else {
                                        println("Searching with DigiPin: $digiPinSearch")
                                        val digiPinToSearch = digiPinSearch
                                        if (digiPinToSearch.isNotEmpty()) {
                                            val (lat, long) = DigiPinUsecase.getLatLngByDigipin(
                                                digiPinSearch
                                            )
                                            if (lat != -1.0 && long != -1.0) {
                                                onSearchClick?.invoke(lat, long)
                                            } else {
                                                println("Invalid DigiPin for search")
                                            }
                                        }
                                    }
                                } else {
                                    isSearchMode = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Toggle Button
                AnimatedVisibility(
                    visible = isSearchMode,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Toggle buttons for Lat Long and DigiPin
                        Text(
                            text = "Lat Long",
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(
                                        bounded = false,
                                    )
                                ) {
                                    isDigiPinSearchMode = false
                                }
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                            style = if (!isDigiPinSearchMode) MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ) else MaterialTheme.typography.bodyLarge
                        )

                        // Divider between toggle buttons
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(38.dp)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        )

                        Text(
                            text = "DigiPin",
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(
                                        bounded = false,
                                    )
                                ) {
                                    isDigiPinSearchMode = true
                                }
                                .padding(8.dp),
                            textAlign = TextAlign.Center,
                            style = if (isDigiPinSearchMode) MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ) else MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Animated visibility for text fields and close button
                AnimatedVisibility(
                    visible = isSearchMode,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    // Text fields for latitude and longitude with close button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically // Align close button with text fields
                    ) {
                        if (isDigiPinSearchMode) {
                            // DigiPin text field with input restriction and validation
                            BasicTextField(
                                value = digiPinSearch,
                                onValueChange = { input ->
                                    val sanitizedInput =
                                        input.filter { it in "fc98j327k456lmptFC98J327K456LMPT-" }
                                    val dashCount = sanitizedInput.count { it == '-' }
                                    if (sanitizedInput.length <= 12 && dashCount <= 2 &&
                                        (dashCount == 0 && sanitizedInput.length <= 10 || dashCount > 0 && sanitizedInput.length <= 12)
                                    ) {
                                        digiPinSearch = sanitizedInput
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(18.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                decorationBox = { innerTextField ->
                                    if (digiPinSearch.isEmpty()) {
                                        Text(
                                            text = "Enter DigiPin",
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                    innerTextField()
                                },
                                singleLine = true
                            )
                        } else {
                            BasicTextField(
                                value = latitude,
                                onValueChange = { input ->
                                    if (input.matches("^-?\\d*\\.?\\d*".toRegex()) && input.length <= 9) {
                                        latitude = input
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(18.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                decorationBox = { innerTextField ->
                                    if (latitude.isEmpty()) {
                                        Text(
                                            text = "Latitude",
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                    innerTextField()
                                },
                                singleLine = true
                            )

                            BasicTextField(
                                value = longitude,
                                onValueChange = { input ->
                                    if (input.matches("^-?\\d*\\.?\\d*".toRegex()) && input.length <= 9) {
                                        longitude = input
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(18.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                decorationBox = { innerTextField ->
                                    if (longitude.isEmpty()) {
                                        Text(
                                            text = "Longitude",
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                    innerTextField()
                                },
                                singleLine = true
                            )
                        }

                        // Close button to exit search mode
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                                .background(
                                    MaterialTheme.colorScheme.error,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    isSearchMode = false

                                    latitude = ""
                                    longitude = ""
                                    digiPinSearch = ""

                                    scope.launch {
                                        delay(300) // Delay to allow the fade-out animation to complete
                                        isDigiPinSearchMode = false
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Search",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
