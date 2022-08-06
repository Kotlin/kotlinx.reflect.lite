package tests.mapping.inlineReifiedFun

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

inline fun <reified T> f() = 1

fun g() {}

class Foo {
    inline fun <reified T> h(t: T) = 1
}

fun box(): String {
    val g = Class.forName("tests.mapping.inlineReifiedFun.InlineReifiedFunKt").kotlinClass.getMemberByName("g") as KFunction<*>
    assertEquals(g as Any?, g.javaMethod!!.kotlinFunction)

    val h = Foo::class.java.kotlinClass.members.single { it.name == "h" } as KFunction<*>
    assertEquals(h, h.javaMethod!!.kotlinFunction as Any?)

    return "OK"
}