package com.moriatsushi.compose.callable.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moriatsushi.compose.callable.CallableHost
import com.moriatsushi.compose.callable.CallableState
import kotlinx.coroutines.launch

@Composable
internal fun SampleScreen(modifier: Modifier = Modifier) {
    val dialogState = remember { CallableState<String, Boolean>() }
    var confirmed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    CallableHost(dialogState) {
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
        Button(
            onClick = {
                coroutineScope.launch {
                    confirmed = dialogState.call("Sure?")
                }
            },
        ) {
            Text("Submit")
        }
        Text(
            text = "Result: ${if (confirmed) "Confirmed" else "Not confirmed"}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
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
