package com.moriatsushi.compose.callable

internal class CallableHostScopeImpl<in R>(
    private val data: CallableData<*, R>,
) : CallableHostScope<R> {
    override fun resume(result: R) = data.resume(result)
}
