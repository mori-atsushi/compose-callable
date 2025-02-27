package com.moriatsushi.compose.callable.sample

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.moriatsushi.compose.callable.CallableState
import com.moriatsushi.compose.callable.ConflictStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class SampleScreenState(
    private val coroutineScope: CoroutineScope,
) {
    private var toastCount = 0

    val yesNoDialogState = CallableState<String, Boolean>()
    val confirmDialogState = CallableState<String, Unit>()
    val bottomNotificationState = CallableState<String, Boolean>()
    val toastState = CallableState<String, Unit>(ConflictStrategy.ENQUEUE)

    var result: String? by mutableStateOf(null)
        private set

    fun call(target: CallableTarget) {
        result = null
        coroutineScope.launch {
            result =
                when (target) {
                    CallableTarget.YES_NO_DIALOG -> callYesNoDialog()
                    CallableTarget.CONFIRMATION_DIALOG -> callConfirmationDialog()
                    CallableTarget.BOTTOM_NOTIFICATION -> callBottomNotification()
                    CallableTarget.TOAST -> callToast()
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

    private suspend fun callBottomNotification(): String {
        val result = bottomNotificationState.call("This is a bottom notification.")
        return if (result) "Confirmed" else "Cancelled"
    }

    private suspend fun callToast(): String {
        val number = toastCount++
        toastState.call("Toast #$number")
        return "Completed #$number"
    }
}
