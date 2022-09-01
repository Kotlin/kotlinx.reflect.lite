/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.innerClassConstructorWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.full.*

class Outer(val x: String) {
    inner class Inner(val y: String) {
        fun foo() = x + y
    }
}

fun box(): String {
    val innerCtor = (Outer::class.java.kotlin.nestedClasses.single { it.simpleName == "Inner" } as KClass<Outer.Inner>).primaryConstructor
    val inner = innerCtor!!.call(Outer("O"), "K")
    return inner.foo()
}
