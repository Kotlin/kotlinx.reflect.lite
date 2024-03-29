/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.innerClassConstructor

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

class A {
    class Nested(val result: String)
    inner class Inner(val result: String)
}

fun box(): String {
    val nested =
        ((A::class.java).kotlin).nestedClasses.single { it.simpleName == "Nested" } as KClass<A.Nested>
    val inner =
        ((A::class.java).kotlin).nestedClasses.single { it.simpleName == "Inner" } as KClass<A.Inner>
    val aCons = ((A::class.java).kotlin).getPrimaryConstructor()
    val nestedCons = nested.getPrimaryConstructor()
    val innerCons = inner.getPrimaryConstructor()
    return nestedCons.call("O").result + innerCons.call(aCons.call(), "K").result
}
