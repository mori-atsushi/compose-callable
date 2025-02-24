package com.moriatsushi.compose.callable

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CallableStateTest {
    @Test
    fun callableState() = runTest {
        val state = CallableState<String, String>()

        val deferred = async { state.call("input") }
        testScheduler.runCurrent()
        assertEquals("input", state.currentData?.input)

        state.currentData?.resume("result")
        assertEquals("result", deferred.await())
        assertNull(state.currentData)
    }

    @Test
    fun callableState_multipleResumes() = runTest {
        val state = CallableState<String, String>()

        val deferred = async { state.call("input") }
        testScheduler.runCurrent()
        val currentData = state.currentData
        assertNotNull(currentData)

        currentData.resume("result1")
        currentData.resume("result2")

        assertEquals("result1", deferred.await())
    }

    @Test
    fun callableState_conflict_CANCEL_AND_OVERWRITE() = runTest {
        val state = CallableState<String, String>()

        backgroundScope.launch { state.call("input1") }
        testScheduler.runCurrent()
        val data1 = state.currentData
        assertNotNull(data1)
        assertEquals("input1", data1.input)

        backgroundScope.launch { state.call("input2") }
        testScheduler.runCurrent()
        val data2 = state.currentData
        assertNotNull(data2)
        assertEquals("input2", data2.input)
        assertNotEquals(data1.key, data2.key)
    }

    @Test
    fun callableState_conflict_ENQUEUE() = runTest {
        val state = CallableState<String, String>(ConflictStrategy.ENQUEUE)

        backgroundScope.launch { state.call("input1") }
        testScheduler.runCurrent()
        assertEquals("input1", state.currentData?.input)

        backgroundScope.launch { state.call("input2") }
        backgroundScope.launch { state.call("input3") }
        testScheduler.runCurrent()
        // Still processing the first call
        assertEquals("input1", state.currentData?.input)

        state.currentData?.resume("result1")
        testScheduler.runCurrent()
        assertEquals("input2", state.currentData?.input)

        state.currentData?.resume("result2")
        testScheduler.runCurrent()
        assertEquals("input3", state.currentData?.input)
    }

    @Test
    fun callableState_cancel() = runTest {
        val state = CallableState<String, String>()

        val job = launch { state.call("input") }
        testScheduler.runCurrent()
        assertEquals("input", state.currentData?.input)

        job.cancel()
        testScheduler.runCurrent()
        assertNull(state.currentData)
    }
}
