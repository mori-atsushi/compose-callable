package com.moriatsushi.copmose.callable

/**
 * An interface representing the data of a single callable component.
 */
interface CallableData<out I, in R> {
    val key: String

    val input: I

    /**
     * Closes the component and returns the result to the caller.
     * Does nothing if the component is already closed.
     */
    fun resume(result: R)
}
