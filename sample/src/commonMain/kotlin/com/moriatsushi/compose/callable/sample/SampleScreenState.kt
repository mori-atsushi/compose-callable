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
    val yesNoDialogState = CallableState<String, Boolean>()
    val confirmDialogState = CallableState<String, Unit>()

    var result: String? by mutableStateOf(null)
        private set

    fun call(target: CallableTarget) {
        coroutineScope.launch {
            result =
                when (target) {
                    CallableTarget.YES_NO_DIALOG -> callYesNoDialog()
                    CallableTarget.CONFIRMATION_DIALOG -> callConfirmationDialog()
                }
        }
    }

    private suspend fun callYesNoDialog(): String {
        val result = yesNoDialogState.call("Are you enjoying Compose?")
        return if (result) "Yes" else "No"
    }

    private suspend fun callConfirmationDialog(): String {
        confirmDialogState.call("Please press the Confirm button.")
        return "Confirmed"
    }
}
