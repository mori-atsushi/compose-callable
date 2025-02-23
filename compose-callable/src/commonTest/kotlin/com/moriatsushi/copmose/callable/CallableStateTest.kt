package com.moriatsushi.copmose.callable

import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
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
}
