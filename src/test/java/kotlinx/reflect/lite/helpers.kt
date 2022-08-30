/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package helpers

import kotlin.coroutines.*

fun <T> handleResultContinuation(x: (T) -> Unit): Continuation<T> = object: Continuation<T> {
    override val context = EmptyCoroutineContext
    override fun resumeWith(result: Result<T>) {
        x(result.getOrThrow())
    }
}


fun handleExceptionContinuation(x: (Throwable) -> Unit): Continuation<Any?> = object: Continuation<Any?> {
    override val context = EmptyCoroutineContext
    override fun resumeWith(result: Result<Any?>) {
        result.exceptionOrNull()?.let(x)
    }
}

open class EmptyContinuation(override val context: CoroutineContext = EmptyCoroutineContext) : Continuation<Any?> {
    companion object : EmptyContinuation()
    override fun resumeWith(result: Result<Any?>) {
        result.getOrThrow()
    }
}

abstract class ContinuationAdapter<in T> : Continuation<T> {
    override val context: CoroutineContext = EmptyCoroutineContext
    override fun resumeWith(result: Result<T>) {
        if (result.isSuccess) {
            resume(result.getOrThrow())
        } else {
            resumeWithException(result.exceptionOrNull()!!)
        }
    }

    abstract fun resumeWithException(exception: Throwable)
    abstract fun resume(value: T)
}
