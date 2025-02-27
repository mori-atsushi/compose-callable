package com.moriatsushi.compose.callable.sample.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
internal fun YesNoDialog(
    onResult: (Boolean) -> Unit,
    text: String,
) {
    AlertDialog(
        onDismissRequest = { onResult(false) },
        confirmButton = {
            TextButton(onClick = { onResult(true) }) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { onResult(false) }) {
                Text("No")
            }
        },
        text = { Text(text) },
    )
}
