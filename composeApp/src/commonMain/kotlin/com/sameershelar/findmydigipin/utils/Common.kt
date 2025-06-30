package com.sameershelar.findmydigipin.utils

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipEntry

expect fun clipEntryOf(string: String): ClipEntry

expect val ShareIcon: ImageVector

var onSearchClick: ((Double, Double) -> Unit)? = null

var onShareDigipinClick: ((String) -> Unit)? = null

var onGoToMyLocationClick: (() -> Unit)? = null

object CONSTANTS {
    const val IS_USER_GUIDE_SHOWN_KEY = "isUserGuideShown"
}
