package com.moriatsushi.compose.callable.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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

    SampleScreenContent(
        modifier = modifier,
        onCall = screenState::call,
        result = screenState.result,
    )
}
