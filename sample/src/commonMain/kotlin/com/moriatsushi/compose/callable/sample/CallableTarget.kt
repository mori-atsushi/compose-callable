package com.moriatsushi.compose.callable.sample

internal enum class CallableTarget(
    val label: String,
) {
    YES_NO_DIALOG("Yes/No Dialog"),
    CONFIRMATION_DIALOG("Confirmation Dialog"),
    BOTTOM_NOTIFICATION("Bottom Notification"),
    TOAST("Toast"),
}
