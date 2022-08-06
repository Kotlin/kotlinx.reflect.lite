package tests.mapping.types.unit

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun foo(unitParam: Unit, nullableUnitParam: Unit?): Unit {}

var bar: Unit = Unit

fun box(): String {
    assert(Unit::class.java != java.lang.Void.TYPE)

    val foo = Class.forName("tests.mapping.types.unit.UnitKt").kotlinClass.getMemberByName("foo") as KFunction<Unit>
    assertEquals(Unit::class.java, foo.parameters[0].type.javaType)
    assertEquals(Unit::class.java, foo.parameters[1].type.javaType)
    assertEquals(java.lang.Void.TYPE, foo.returnType.javaType)

    val bar = Class.forName("tests.mapping.types.unit.UnitKt").kotlinClass.getMemberByName("bar") as KMutableProperty0<Unit>
    assertEquals(Unit::class.java, bar.returnType.javaType)
    assertEquals(Unit::class.java, bar.getter.returnType.javaType)
    assertEquals(Unit::class.java, bar.setter.parameters.single().type.javaType)
    assertEquals(java.lang.Void.TYPE, bar.setter.returnType.javaType)

    return "OK"
}
