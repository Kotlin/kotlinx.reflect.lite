/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.defaultInSuperClass

open class A {
    open fun foo(a: String, b: String = "b") = b + a
}

class B : A() {
    override fun foo(a: String, b: String) = a + b
}

fun box(): String {
    val f = B::foo

    assert("ab" == f.callBy(mapOf(
        f.parameters.first() to B(),
        f.parameters.single { it.name == "a" } to "a"
    )))

    return "OK"
}
