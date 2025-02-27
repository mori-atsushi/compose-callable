package com.moriatsushi.compose.callable.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moriatsushi.compose.callable.CallableHost

@Composable
internal fun SampleScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val screenState = remember { SampleScreenState(coroutineScope) }

    CallableHost(screenState.dialogState) {
        ConfirmDialog(
            onDismissRequest = { resume(false) },
            onConfirm = { resume(true) },
            text = it,
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { screenState.call() }) {
            Text("Submit")
        }
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = screenState.result?.let { "Result: $it" }.orEmpty(),
        )
    }
}

@Composable
private fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    text: String,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        text = { Text(text) },
    )
}
