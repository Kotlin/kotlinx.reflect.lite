package tests.call.returnUnit

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun foo() {}

class A {
    fun bar() {}
}

object O {
    @JvmStatic fun baz() {}
}

fun nullableUnit(unit: Boolean): Unit? = if (unit) Unit else null

fun box(): String {
    val clazz = Class.forName("tests.call.returnUnit.ReturnUnitKt").kotlinClass
    val foo = clazz.getMemberByName("foo")
    assertEquals(Unit, foo.call())
    val bar = (A::class.java.kotlinClass as KClass<A>).getMemberByName("bar")
    assertEquals(Unit, bar.call(A()))
    assertEquals(Unit, ((O::class.java.kotlinClass as KClass<O>)).members.single { it.name == "baz" }.call(O))

    val nullableUnit = clazz.getMemberByName("nullableUnit")
    assertEquals(Unit, nullableUnit.call(true))
    assertEquals(null, nullableUnit.call(false))

    return "OK"
}
