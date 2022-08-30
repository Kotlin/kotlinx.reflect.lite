/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

// Copy of: https://github.com/JetBrains/kotlin/blob/master/core/reflection.jvm/src/kotlin/reflect/jvm/internal/calls/Caller.kt
package kotlinx.reflect.lite.calls

import java.lang.reflect.Member
import java.lang.reflect.Type

internal interface Caller<out M : Member?> {
    val member: M

    val returnType: Type

    val parameterTypes: List<Type>

    fun checkArguments(args: Array<*>) {
        if (arity != args.size) {
            throw IllegalArgumentException("Callable expects $arity arguments, but ${args.size} were provided.")
        }
    }

    fun call(args: Array<*>): Any?
}

internal val Caller<*>.arity: Int
    get() = parameterTypes.size
