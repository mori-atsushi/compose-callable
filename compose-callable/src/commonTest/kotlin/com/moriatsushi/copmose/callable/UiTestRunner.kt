package com.moriatsushi.copmose.callable

import kotlin.reflect.KClass

expect annotation class RunWith(
    val value: KClass<out Runner>,
)

expect abstract class Runner

expect class UiTestRunner : Runner
