package com.moriatsushi.compose.callable.sample.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNotification(
    text: String,
    onResult: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = text,
                fontSize = 13.sp,
            )
            Row {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onResult(false) },
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onResult(true) },
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}
