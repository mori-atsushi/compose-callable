package com.moriatsushi.compose.callable.sample.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@Composable
internal fun ConfirmDialog(
    text: String,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {}, // This dialog is not dismissible.
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        text = { Text(text) },
        properties =
            DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
    )
}
