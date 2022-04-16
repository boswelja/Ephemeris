package com.boswelja.ephemeris.views

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

fun <T> StateFlow<T>.getOrAwaitValue(timeout: Long): T {
    return runBlocking {
        withTimeoutOrNull(timeout) {
            this@getOrAwaitValue.second()
        } ?: value
    }
}

suspend fun <T> Flow<T>.second(): T {
    var value: T? = null
    take(2).collect {
        value = it
    }
    return value!!
}
