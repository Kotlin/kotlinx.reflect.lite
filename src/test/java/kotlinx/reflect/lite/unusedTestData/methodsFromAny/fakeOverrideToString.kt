/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.methodsFromAny.fakeOverrideToString

import kotlin.test.assertEquals

open class A<T> {
    fun foo(t: T) {}
}

open class B<U> : A<U>()

class C : B<String>()

fun box(): String {
    assertEquals("fun tests.methodsFromAny.fakeOverrideToString.A<T>.foo(T): kotlin.Unit", A<Double>::foo.toString())
    assertEquals("fun tests.methodsFromAny.fakeOverrideToString.B<U>.foo(U): kotlin.Unit", B<Float>::foo.toString())
    assertEquals("fun tests.methodsFromAny.fakeOverrideToString.C.foo(kotlin.String): kotlin.Unit", C::foo.toString())

    val afoo = A::class.members.single { it.name == "foo" }
    assertEquals("fun tests.methodsFromAny.fakeOverrideToString.A<T>.foo(T): kotlin.Unit", afoo.toString())
    val bfoo = B::class.members.single { it.name == "foo" }
    assertEquals("fun tests.methodsFromAny.fakeOverrideToString.B<U>.foo(U): kotlin.Unit", bfoo.toString())
    val cfoo = C::class.members.single { it.name == "foo" }
    assertEquals("fun tests.methodsFromAny.fakeOverrideToString.C.foo(kotlin.String): kotlin.Unit", cfoo.toString())

    return "OK"
}
