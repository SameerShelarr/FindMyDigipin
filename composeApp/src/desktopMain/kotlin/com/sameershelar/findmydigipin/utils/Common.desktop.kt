package com.sameershelar.findmydigipin.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipEntry

actual fun clipEntryOf(string: String): ClipEntry {
    return ClipEntry(string)
}

actual val ShareIcon: ImageVector
    get() = Icons.Default.Share
