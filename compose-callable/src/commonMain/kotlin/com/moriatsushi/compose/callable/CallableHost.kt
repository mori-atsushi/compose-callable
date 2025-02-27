package com.moriatsushi.compose.callable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember

@Composable
fun <I, R> CallableHost(
    state: CallableState<I, R>,
    content: @Composable CallableHostScope<R>.(I) -> Unit,
) {
    val currentData = state.currentData
    if (currentData != null) {
        key(currentData.key) {
            val scope = remember(currentData) { CallableHostScopeImpl(currentData) }
            scope.content(currentData.input)
        }
    }
}

interface CallableHostScope<in R> {
    /**
     * Closes the component and returns the result to the caller.
     * Does nothing if the component is already closed.
     */
    fun resume(result: R)
}

/**
 * Closes the component without returning any result.
 * @see [CallableHostScope.resume]
 */
fun CallableHostScope<Unit>.resume() = resume(Unit)

private class CallableHostScopeImpl<in R>(
    private val data: CallableData<*, R>,
) : CallableHostScope<R> {
    override fun resume(result: R) = data.resume(result)
}
