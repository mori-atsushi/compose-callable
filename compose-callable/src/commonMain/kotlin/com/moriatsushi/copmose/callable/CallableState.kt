package com.moriatsushi.copmose.callable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * The state of a callable component, such as a dialog, popup, or modal.
 */
interface CallableState<I, R> {
    val currentData: CallableData<I, R>?

    /**
     * Calls the component with the given [input].
     * After the component is closed, the result is returned.
     * If the component is already open, the previous call is canceled.
     */
    suspend fun call(input: I): R
}

fun <I, R> CallableState(): CallableState<I, R> = CallableStateImpl()

private class CallableStateImpl<I, R> : CallableState<I, R> {
    private val mutex = Mutex()
    private var currentJob: Job? = null

    override var currentData: CallableData<I, R>? by mutableStateOf(null)
        private set

    override suspend fun call(input: I): R = try {
        mutex.withLock {
            currentJob?.cancelAndJoin()
            currentJob = coroutineContext[Job]
        }
        suspendCancellableCoroutine { continuation ->
            @OptIn(ExperimentalUuidApi::class)
            val key = Uuid.random().toString()
            currentData = CallableDataImpl(key, input, continuation)
        }
    } finally {
        currentData = null
        currentJob = null
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
