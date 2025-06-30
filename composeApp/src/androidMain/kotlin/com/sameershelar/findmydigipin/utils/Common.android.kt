package com.sameershelar.findmydigipin.utils

import android.content.ClipData
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipEntry

actual fun clipEntryOf(string: String): ClipEntry {
    return ClipEntry(
        clipData = ClipData(
            "",
            arrayOf("text/plain"),
            ClipData.Item(string)
        )
    )
}

actual val ShareIcon: ImageVector
    get() = Icons.Default.Share
