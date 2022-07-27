package tests.builtins.enumNameOrdinal

import kotlin.test.assertEquals
import kotlin.test.assertTrue

enum class E { X, Y, Z }

fun box(): String {
    assertTrue(E::class.members.size in 11..12, "" + E::class.members.size)
    assertEquals("Y", E::name.call(E.Y))
    assertEquals(2, E::ordinal.call(E.Z))
    return "OK"
}