package com.moriatsushi.copmose.callable

/**
 * The state of a callable component, such as a dialog, popup, or modal.
 */
interface CallableState<I, R> {
    val currentData: CallableData<I, R>?

    suspend fun call(input: I): R
}
