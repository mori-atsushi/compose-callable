package com.moriatsushi.copmose.callable

import kotlin.reflect.KClass

actual annotation class RunWith(
    actual val value: KClass<out Runner>,
)

actual abstract class Runner

actual class UiTestRunner : Runner()
