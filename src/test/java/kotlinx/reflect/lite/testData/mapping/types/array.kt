package tests.mapping.types.array

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import java.lang.reflect.GenericArrayType
import java.lang.reflect.TypeVariable
import java.lang.reflect.ParameterizedType
import kotlin.test.assertEquals

fun foo(strings: Array<String>, integers: Array<Int>, objectArrays: Array<Array<Any>>) {}

fun bar(): Array<List<String>> = null!!
class A<T> {
    fun baz(): Array<T> = null!!
}

fun box(): String {
    val foo = Class.forName("tests.mapping.types.array.ArrayKt").kDeclarationContainer.getMemberByName("foo")
    assertEquals(Array<String>::class.java, foo.parameters[0].type.javaType)
    assertEquals(Array<Int>::class.java, foo.parameters[1].type.javaType)
    assertEquals(Array<Array<Any>>::class.java, foo.parameters[2].type.javaType)

    val bar = Class.forName("tests.mapping.types.array.ArrayKt").kDeclarationContainer.getMemberByName("bar")
    val g = bar.returnType.javaType
    if (g !is GenericArrayType || g.genericComponentType !is ParameterizedType)
        return "Fail: should be array of parameterized type, but was $g (${g.javaClass})"

    val baz = A::class.java.kDeclarationContainer.getMemberByName("baz") as KFunction<Array<String>>
    val h = baz.returnType.javaType
    if (h !is GenericArrayType || h.genericComponentType !is TypeVariable<*>)
        return "Fail: should be array of type variable, but was $h (${h.javaClass})"

    return "OK"
}
