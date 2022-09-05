/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.boundReferencesWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

class C {
    fun foo() {}
    var bar = "OK"
}

fun C.extFun(i: Int) {}

fun KParameter.check(name: String) {
    assertEquals(name, this.name!!)
    assertEquals(KParameter.Kind.VALUE, this.kind)
}

fun box(): String {
    val clazz = Class.forName("tests.parameters.boundReferencesWA.BoundReferencesWAKt").kotlinPackage
    val cFoo = C::class.java.kotlin.getMemberByName("foo")
    val cBar = C::class.java.kotlin.getMemberByName("bar") as KMutableProperty1<C, String>
    val cExtFun = clazz.getMemberByName("extFun")

    assertEquals(1, cFoo.parameters.size)
    assertEquals(1, cBar.getter.parameters.size)
    assertEquals(2, cBar.setter.parameters.size)

    assertEquals(2, cExtFun.parameters.size)
    cExtFun.parameters[1].check("i")

    return "OK"
}
