package tests.parameters.functionParameterNameAndIndex

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun foo(bar: String): Int = bar.length

class A(val c: String) {
    fun foz(baz: Int) {}

    fun Double.mext(mez: Long) {}
}

fun Int.qux(zux: String) {}

fun checkParameters(f: KCallable<*>, names: List<String?>) {
    val params = f.parameters
    assertEquals(names, params.map { it.name })
    assertEquals(params.indices.toList(), params.map { it.index })
}

fun box(): String {
    val clazz = Class.forName("tests.parameters.functionParameterNameAndIndex.FunctionParameterNameAndIndexKt").kotlinClass
    checkParameters(clazz.getMemberByName("box"), listOf())
    checkParameters(clazz.getMemberByName("foo"), listOf("bar"))
    checkParameters((A::class.java).kotlinClass.getMemberByName("foz"), listOf(null, "baz"))
    checkParameters(clazz.getMemberByName("qux"), listOf(null, "zux"))

    checkParameters((A::class.java).kotlinClass.getMemberByName("mext"), listOf(null, null, "mez"))

    checkParameters(((A::class.java).kotlinClass as KClass<A>).constructors.first(), listOf("c"))

    return "OK"
}
