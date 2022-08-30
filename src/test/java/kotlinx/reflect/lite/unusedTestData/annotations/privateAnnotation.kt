/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.privateAnnotation

annotation private class Ann(val name: String)

class A {
    @Ann("OK")
    fun foo() {}
}

fun box(): String {
    val ann = A::class.members.single { it.name == "foo" }.annotations.single() as Ann
    return ann.name
}
