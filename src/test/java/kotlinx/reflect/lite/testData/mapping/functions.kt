package tests.mapping.functions

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*

class K {
    fun foo(s: String): Int = s.length
}
fun bar(s: String): Int = s.length
fun String.baz(): Int = this.length

fun check(f: KFunction<Int>) {
    assert(f.javaConstructor == null) { "Fail f constructor" }
    assert(f.javaMethod != null) { "Fail f method" }
    val m = f.javaMethod!!

    assert(m.kotlinFunction != null) { "Fail m function" }
    val ff = m.kotlinFunction!!

    assert(f == ff) { "Fail f != ff" }
}

fun box(): String {
    check((K::class.java.kotlinClass).getMemberByName("foo") as KFunction<Int>)
    val clazz = Class.forName("tests.mapping.functions.FunctionsKt").kotlinClass
    check(clazz.getMemberByName("bar") as KFunction<Int>)
    check(clazz.getMemberByName("baz") as KFunction<Int>)

    return "OK"
}
