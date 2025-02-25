package com.moriatsushi.compose.callable

import androidx.collection.mutableScatterMapOf
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <I, R> AnimatedCallableHost(
    state: CallableState<I, R>,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = fadeOut() + shrinkOut(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable CallableHostScope<R>.(I) -> Unit,
) {
    val transition = updateTransition(state.currentData)

    transition.AnimatedHost(
        modifier = modifier,
        enter = enter,
        exit = exit,
        contentKey = { it.key },
        contentAlignment = contentAlignment,
    ) { targetData ->
        key(targetData.key) {
            val scope = remember(targetData) { CallableHostScopeImpl(targetData) }
            scope.content(targetData.input)
        }
    }
}

@Composable
private fun <S : Any> Transition<S?>.AnimatedHost(
    modifier: Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = fadeOut() + shrinkOut(),
    contentKey: (targetState: S) -> Any = { it },
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable (targetState: S) -> Unit,
) {
    val currentlyVisible = setOfNotNull(currentState, targetState)
    val contentMap =
        remember(currentlyVisible) {
            val contentMap = mutableScatterMapOf<S, @Composable () -> Unit>()
            currentlyVisible.forEach { stateForContent ->
                contentMap[stateForContent] = {
                    AnimatedVisibility(
                        visible = { it == stateForContent },
                        enter = enter,
                        exit = exit,
                    ) {
                        content(stateForContent)
                    }
                }
            }
            contentMap
        }

    Box(modifier = modifier, contentAlignment = contentAlignment) {
        currentlyVisible.forEach {
            key(contentKey(it)) {
                contentMap[it]?.invoke()
            }
        }
    }
}
