package com.moriatsushi.compose.callable.sample

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moriatsushi.compose.callable.AnimatedCallableHost
import com.moriatsushi.compose.callable.CallableHost
import com.moriatsushi.compose.callable.resume
import com.moriatsushi.compose.callable.sample.component.BottomNotification
import com.moriatsushi.compose.callable.sample.component.ConfirmDialog
import com.moriatsushi.compose.callable.sample.component.Toast
import com.moriatsushi.compose.callable.sample.component.YesNoDialog
import kotlinx.coroutines.delay

@Composable
internal fun SampleScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val screenState = remember { SampleScreenState(coroutineScope) }

    CallableHost(screenState.yesNoDialogState) {
        YesNoDialog(
            text = it,
            onResult = ::resume,
        )
    }

    CallableHost(screenState.confirmDialogState) {
        ConfirmDialog(text = it, onConfirm = ::resume)
    }

    Box(modifier = modifier) {
        SampleScreenContent(
            onCall = screenState::call,
            result = screenState.result,
        )

        AnimatedCallableHost(
            modifier = Modifier.fillMaxSize(),
            state = screenState.bottomNotificationState,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
            contentAlignment = Alignment.BottomCenter,
        ) {
            BottomNotification(
                modifier =
                    Modifier
                        .systemBarsPadding()
                        .displayCutoutPadding(),
                text = it,
                onResult = ::resume,
            )
        }

        AnimatedCallableHost(
            modifier = Modifier.fillMaxSize().systemBarsPadding(),
            state = screenState.toastState,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f),
            contentAlignment = Alignment.TopCenter,
        ) {
            LaunchedEffect(Unit) {
                delay(2400)
                resume(Unit)
            }

            Toast(text = it, onClick = ::resume)
        }
    }
}
