/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.defaultInSuperInterface

interface A1 {
    fun test(o: String, k: String): String
}
interface A2 : A1

interface B1 {
    fun test(o: String = "O", k: String = "K"): String
}
interface B2 : B1

interface C1
interface C2 : C1

interface D
interface E : A2, B2, C2 {
    override fun test(o: String, k: String): String {
        return o + k
    }
}

class Z : D, E

fun box(): String {
    val f = Z::test
    return f.callBy(mapOf(f.parameters.first() to Z()))
}
