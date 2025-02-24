package com.moriatsushi.copmose.callable

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class CallableMutex(
    private val onConflict: ConflictStrategy,
) {
    private val mutex = Mutex()
    private val currentJob = atomic<Job?>(null)

    suspend fun <R> withLock(block: suspend () -> R): R = coroutineScope {
        val job = coroutineContext[Job]!!

        if (onConflict == ConflictStrategy.CANCEL_AND_OVERWRITE) {
            cancelCurrentJob(job)
        }

        // Since a suspending Mutex acts as a fair queue, we don't need to explicitly enqueue the
        // job, even when the `onConflict` strategy is set to `ENQUEUE`.
        mutex.withLock {
            try {
                block()
            } finally {
                currentJob.compareAndSet(job, null)
            }
        }
    }

    private fun cancelCurrentJob(nextJob: Job) {
        val currentJob = currentJob.getAndSet(nextJob)
        currentJob?.cancel()
    }
}
