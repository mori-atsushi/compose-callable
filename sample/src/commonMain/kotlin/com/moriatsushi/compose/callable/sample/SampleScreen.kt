package com.moriatsushi.compose.callable.sample

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moriatsushi.compose.callable.CallableHost
import com.moriatsushi.compose.callable.sample.component.ConfirmDialog
import com.moriatsushi.compose.callable.sample.component.YesNoDialog

@Composable
internal fun SampleScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val screenState = remember { SampleScreenState(coroutineScope) }

    CallableHost(screenState.yesNoDialogState) {
        YesNoDialog(
            text = it,
            onResult = ::resume,
        )
    }
    CallableHost(screenState.confirmDialogState) {
        ConfirmDialog(
            text = it,
            onConfirm = { resume(Unit) },
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Title(modifier = Modifier.padding(bottom = 32.dp))
        Row {
            TargetSelector(
                modifier = Modifier.padding(end = 10.dp),
                selectedTarget = screenState.selectedTarget,
                onSelectedTargetChange = screenState::selectTarget,
            )
            Button(onClick = { screenState.call() }) {
                Text("Call")
            }
        }
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = screenState.result?.let { "Result: $it" }.orEmpty(),
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Compose Callable",
            fontSize = 24.sp,
        )
        Text(
            modifier = Modifier.padding(vertical = 2.dp),
            text = "https://github.com/mori-atsushi/compose-callable",
            fontSize = 12.sp,
            color = Color.Gray,
        )
    }
}

@Composable
private fun TargetSelector(
    selectedTarget: CallableTarget,
    onSelectedTargetChange: (CallableTarget) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            modifier = modifier.width(200.dp),
            onClick = { isMenuExpanded = true },
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 4.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = selectedTarget.label,
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
        ) {
            for (target in CallableTarget.entries) {
                DropdownMenuItem(
                    text = { Text(target.label) },
                    onClick = {
                        onSelectedTargetChange(target)
                        isMenuExpanded = false
                    },
                )
            }
        }
    }
}
