package tests.constructors.simpleGetConstructors

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import java.util.Collections
import kotlin.test.assertEquals
import kotlin.test.assertTrue

open class A private constructor(x: Int) {
    public constructor(s: String): this(s.length)
    constructor(): this("")
}

class B : A("")

class C {
    class Nested
    inner class Inner
}

fun box(): String {
    assertEquals(3, (A::class.java.kotlinClass as KClass<A>).constructors.size)
    assertEquals(1, (B::class.java.kotlinClass as KClass<B>).constructors.size)

    assertTrue(Collections.disjoint(
        (A::class.java.kotlinClass as KClass<A>).members,
        (A::class.java.kotlinClass as KClass<A>).constructors
    ))
    assertTrue(Collections.disjoint(
        (B::class.java.kotlinClass as KClass<B>).members,
        (B::class.java.kotlinClass as KClass<B>).constructors
    ))

    assertEquals(1, (C.Nested::class.java.kotlinClass as KClass<C.Nested>).constructors.size)
    assertEquals(1, (C.Inner::class.java.kotlinClass as KClass<C.Inner>).constructors.size)

    return "OK"
}
