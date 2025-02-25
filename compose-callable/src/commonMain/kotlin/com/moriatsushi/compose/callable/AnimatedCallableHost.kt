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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach

@Composable
fun <I, R> AnimatedCallableHost(
    state: CallableState<I, R>,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = fadeOut() + shrinkOut(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable CallableHostScope<R>.(I) -> Unit,
) {
    val currentData = state.currentData
    val transition = updateTransition(currentData)

    transition.AnimatedHost(
        modifier = modifier,
        enter = enter,
        exit = exit,
        contentKey = { it?.key },
        contentAlignment = contentAlignment,
    ) { targetData ->
        if (targetData != null) {
            key(targetData.key) {
                val scope = remember(targetData) { CallableHostScopeImpl(targetData) }
                scope.content(targetData.input)
            }
        }
    }
}

@Composable
private fun <S> Transition<S>.AnimatedHost(
    modifier: Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = fadeOut() + shrinkOut(),
    contentKey: (targetState: S) -> Any? = { it },
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable (targetState: S) -> Unit,
) {
    val currentlyVisible = remember { mutableStateListOf(currentState) }
    val contentMap = remember { mutableScatterMapOf<S, @Composable () -> Unit>() }

    if (!currentlyVisible.contains(currentState)) {
        currentlyVisible.clear()
        currentlyVisible.add(currentState)
    }

    if (currentState == targetState) {
        if (currentlyVisible.size != 1 || currentlyVisible[0] != currentState) {
            currentlyVisible.clear()
            currentlyVisible.add(currentState)
        }
        if (contentMap.size != 1 || contentMap.containsKey(currentState)) {
            contentMap.clear()
        }
    }

    if (currentState != targetState && !currentlyVisible.contains(targetState)) {
        currentlyVisible.add(targetState)
    }

    if (!contentMap.containsKey(targetState) || !contentMap.containsKey(currentState)) {
        contentMap.clear()
        currentlyVisible.fastForEach { stateForContent ->
            contentMap[stateForContent] = {
                AnimatedVisibility(
                    visible = { it == stateForContent },
                    enter = enter,
                    exit = exit,
                ) {
                    DisposableEffect(this) {
                        onDispose {
                            currentlyVisible.remove(stateForContent)
                        }
                    }
                    content(stateForContent)
                }
            }
        }
    }

    Box(modifier = modifier, contentAlignment = contentAlignment) {
        currentlyVisible.fastForEach {
            key(contentKey(it)) {
                contentMap[it]?.invoke()
            }
        }
    }
}
