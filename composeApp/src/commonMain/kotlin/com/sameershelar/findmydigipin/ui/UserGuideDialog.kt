package com.sameershelar.findmydigipin.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun UserGuideDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "User Guide") },
            text = {
                Text(
                    text = "1. Long click on map to get DigiPin of that location.\n\n" +
                            "2. Click search button to expand search options.\n\n" +
                            "3. Enter lat long or DigiPin then click search again.\n\n" +
                            "4. Share or copy DigiPin for easy access.\n\n" +
                            "5. Access this guide from \"?\" icon.\n\n"
                )
            },
            titleContentColor = MaterialTheme.colorScheme.primary,
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("Got it")
                }
            }
        )
    }
}