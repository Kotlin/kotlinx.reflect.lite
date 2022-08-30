package tests.classes.classMembers

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.*

import kotlin.test.*


open class Base {
    fun foo() {}

    val a = 20
}

class Derived : Base() {
    val b = 20

    fun bar() {}

    companion object {
        fun staticFun() {}
    }
}


fun members(c: KClass<*>) = c.members.map { it.name }.sorted()

fun box(): String {
    assertEquals(listOf("a", "b", "bar", "equals", "foo", "hashCode", "toString"), members((Derived::class.java).kotlin))
    assertEquals(listOf("equals", "hashCode", "staticFun", "toString"), members((Derived.Companion::class.java).kotlin))
    return "OK"
}

