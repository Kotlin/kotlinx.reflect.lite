/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.methodsFromAny.fakeOverrideEqualsHashCode

import kotlin.test.assertNotEquals

open class A<T> {
    fun foo(t: T) {}
}

open class B<U> : A<U>()

class C : B<String>()

fun box(): String {
    val afoo = A::class.members.single { it.name == "foo" }
    val bfoo = B::class.members.single { it.name == "foo" }
    val cfoo = C::class.members.single { it.name == "foo" }

    assertNotEquals(afoo, bfoo)
    assertNotEquals(afoo.hashCode(), bfoo.hashCode())
    assertNotEquals(bfoo, cfoo)
    assertNotEquals(bfoo.hashCode(), cfoo.hashCode())

    return "OK"
}
