/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callSuspend.primitiveSuspendFunctions

import kotlin.coroutines.startCoroutine
import kotlin.test.assertEquals
import helpers.*
import kotlinx.reflect.lite.full.*
import kotlinx.reflect.lite.jvm.*


class Z(val value: Int)

class C {
    private var value: Z = Z(0)

    suspend fun nonNullConsume(z: Z) { value = z }
    suspend fun nonNullProduce(): Z = value
    suspend fun nullableConsume(z: Z?) { value = z!! }
    suspend fun nullableProduce(): Z? = value
    suspend fun nonNull_nonNullConsumeAndProduce(z: Z): Z = z
    suspend fun nonNull_nullableConsumeAndProduce(z: Z): Z? = z
    suspend fun nullable_nonNullConsumeAndProduce(z: Z?): Z = z!!
    suspend fun nullable_nullableConsumeAndProduce(z: Z?): Z? = z
}

private fun run0(f: suspend () -> Int): Int {
    var result = -1
    f.startCoroutine(handleResultContinuation { result = it })
    return result
}

fun box(): String {
    val c = C()

    run0 {
        val nonNullConsume = (C::class.java).kotlin.members.single { it.name == "nonNullConsume" }
        val nonNullProduce = (C::class.java).kotlin.members.single { it.name == "nonNullProduce" }
        nonNullConsume.callSuspend(c, Z(1))
        (nonNullProduce.callSuspend(c) as Z).value
    }.let { assertEquals(1, it) }

    run0 {
        val nullableConsume = (C::class.java).kotlin.members.single { it.name == "nullableConsume" }
        nullableConsume.callSuspend(c, Z(2))
        val nullableProduce = (C::class.java).kotlin.members.single { it.name == "nullableProduce" }
        (nullableProduce.callSuspend(c)!! as Z).value
    }.let { assertEquals(2, it) }

    run0 {
        val nonNull_nonNullConsumeAndProduce = (C::class.java).kotlin.members.single { it.name == "nonNull_nonNullConsumeAndProduce" }
        (nonNull_nonNullConsumeAndProduce.callSuspend(c, Z(3)) as Z).value
    }.let { assertEquals(3, it) }

    run0 {
        val nonNull_nullableConsumeAndProduce = (C::class.java).kotlin.members.single { it.name == "nonNull_nullableConsumeAndProduce" }
        (nonNull_nullableConsumeAndProduce.callSuspend(c, Z(4))!! as Z).value
    }.let { assertEquals(4, it) }

    run0 {
        val nullable_nonNullConsumeAndProduce = (C::class.java).kotlin.members.single { it.name == "nullable_nonNullConsumeAndProduce" }
        (nullable_nonNullConsumeAndProduce.callSuspend(c, Z(5)) as Z).value
    }.let { assertEquals(5, it) }

    run0 {
        val nullable_nullableConsumeAndProduce = (C::class.java).kotlin.members.single { it.name == "nullable_nullableConsumeAndProduce" }
        (nullable_nullableConsumeAndProduce.callSuspend(c, Z(6))!! as Z).value
    }.let { assertEquals(6, it) }

    return "OK"
}
