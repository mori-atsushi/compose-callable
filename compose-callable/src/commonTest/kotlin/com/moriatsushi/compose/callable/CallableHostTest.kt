package com.moriatsushi.compose.callable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(UiTestRunner::class)
@OptIn(ExperimentalTestApi::class)
class CallableHostTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val coroutineScope = TestScope(UnconfinedTestDispatcher())

    @Test
    fun callableHost() = runComposeUiTest {
        val state = CallableState<String, String>()
        setContent {
            CallableHost(state) { input ->
                BasicText(
                    modifier = Modifier.clickable { resume("result") },
                    text = input,
                )
            }
        }

        var result: String? = null
        coroutineScope.launch {
            result = state.call("input")
        }

        onNodeWithText("input")
            .assertExists()
            .performClick()

        assertEquals("result", result)

        onNodeWithText("input")
            .assertDoesNotExist()
    }
}
