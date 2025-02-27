package com.moriatsushi.compose.callable.sample

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.moriatsushi.compose.callable.CallableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class SampleScreenState(
    private val coroutineScope: CoroutineScope,
) {
    var selectedTarget by mutableStateOf(CallableTarget.CONFIRMATION_DIALOG)
        private set

    val confirmDialogState = CallableState<String, Unit>()

    var result: String? by mutableStateOf(null)
        private set

    fun call() {
        coroutineScope.launch {
            result = when (selectedTarget) {
                CallableTarget.CONFIRMATION_DIALOG -> callConfirmationDialog()
            }
        }
    }

    private suspend fun callConfirmationDialog(): String {
        confirmDialogState.call("Please press the Confirm button.")
        return "Confirmed"
    }

    fun selectTarget(target: CallableTarget) {
        selectedTarget = target
    }
}
