package tests.constructors.simpleGetConstructors

import kotlinx.reflect.lite.tests.*
import java.util.Collections
import kotlin.reflect.*
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
    assertEquals(3, A::class.java.toLiteKClass().constructors.size)
    assertEquals(1, B::class.java.toLiteKClass().constructors.size)

    assertTrue(Collections.disjoint(A::class.java.toLiteKClass().members, A::class.java.toLiteKClass().constructors))
    assertTrue(Collections.disjoint(B::class.java.toLiteKClass().members, B::class.java.toLiteKClass().constructors))

    assertEquals(1, C.Nested::class.java.toLiteKClass().constructors.size)
    assertEquals(1, C.Inner::class.java.toLiteKClass().constructors.size)

    return "OK"
}