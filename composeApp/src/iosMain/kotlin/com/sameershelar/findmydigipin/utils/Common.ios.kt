package com.sameershelar.findmydigipin.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipEntry

@OptIn(ExperimentalComposeUiApi::class)
actual fun clipEntryOf(string: String): ClipEntry {
    return ClipEntry.withPlainText(string)
}

actual val ShareIcon: ImageVector
    get() = Icons.Default.IosShare
