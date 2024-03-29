/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.boundReferences

import kotlin.reflect.*
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
    val cFoo = C()::foo
    val cBar = C()::bar
    val cExtFun = C()::extFun

    assertEquals(0, cFoo.parameters.size)
    assertEquals(0, cBar.getter.parameters.size)
    assertEquals(1, cBar.setter.parameters.size)

    assertEquals(1, cExtFun.parameters.size)
    cExtFun.parameters[0].check("i")

    return "OK"
}
