/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.functions

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

class K {
    fun foo(s: String): Int = s.length
}
fun bar(s: String): Int = s.length
fun String.baz(): Int = this.length

fun check(f: KFunction<Int>) {
    assert(f.javaConstructor == null) { "Fail f constructor" }
    assert(f.javaMethod != null) { "Fail f method" }
    val m = f.javaMethod!!

    assert(m.kotlinFunction != null) { "Fail m function" }
    val ff = m.kotlinFunction!!

    assert(f == ff) { "Fail f != ff" }
}

fun box(): String {
    check((K::class.java.kDeclarationContainer).getMemberByName("foo") as KFunction<Int>)
    val clazz = Class.forName("tests.mapping.functions.FunctionsKt").kDeclarationContainer
    check(clazz.getMemberByName("bar") as KFunction<Int>)
    check(clazz.getMemberByName("baz") as KFunction<Int>)

    return "OK"
}
