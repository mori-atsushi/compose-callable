package com.moriatsushi.compose.callable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * The state of a callable component, such as a dialog, popup, or modal.
 *
 * @param I The type of the input passed to the callable component.
 * @param R The type of the result returned by the callable component.
 */
interface CallableState<I, R> {
    /**
     * The current call data. This will be non-null while the component is active
     * (for example, when a dialog is visible) and null when there is no active call.
     */
    val currentData: CallableData<I, R>?

    /**
     * Calls the component with the given [input].
     *
     * After the component is closed, the result is returned.
     * If the caller's coroutine scope is canceled, the component is also closed.
     *
     * Depending on the chosen [ConflictStrategy], if the component is already active:
     * - [ConflictStrategy.CANCEL_AND_OVERWRITE]: The current call is canceled and immediately
     * overwritten.
     * - [ConflictStrategy.ENQUEUE]: The new call is enqueued to be processed after the current
     * call completes.
     */
    suspend fun call(input: I): R
}

/**
 * Creates a new [CallableState] with the given [onConflict] strategy.
 *
 * @param I The type of the input passed to the callable component.
 * @param R The type of the result returned by the callable component.
 * @param onConflict The strategy to use when a new call is initiated while another call is active.
 * Defaults to [ConflictStrategy.CANCEL_AND_OVERWRITE].
 */
fun <I, R> CallableState(
    onConflict: ConflictStrategy = ConflictStrategy.CANCEL_AND_OVERWRITE,
): CallableState<I, R> = CallableStateImpl(onConflict)

/**
 * The strategy for handling a new call when a previous call is still active.
 */
enum class ConflictStrategy {
    /**
     * If a call is already in progress, cancel it and immediately start processing the new call.
     */
    CANCEL_AND_OVERWRITE,

    /**
     * If a call is already in progress, enqueue the new call and process it only after the current
     * call completes.
     */
    ENQUEUE,
}

private class CallableStateImpl<I, R>(
    onConflict: ConflictStrategy,
) : CallableState<I, R> {
    private val mutex = CallableMutex(onConflict)

    override var currentData: CallableData<I, R>? by mutableStateOf(null)
        private set

    override suspend fun call(input: I): R = mutex.withLock {
        try {
            suspendCancellableCoroutine { continuation ->
                @OptIn(ExperimentalUuidApi::class)
                val key = Uuid.random().toString()
                currentData = CallableDataImpl(key, input, continuation)
            }
        } finally {
            currentData = null
        }
    }
}

private class CallableDataImpl<I, R>(
    override val key: String,
    override val input: I,
    private val continuation: CancellableContinuation<R>,
) : CallableData<I, R> {
    override fun resume(result: R) {
        if (continuation.isActive) {
            continuation.resume(result)
        }
    }
}
