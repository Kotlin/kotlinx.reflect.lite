package tests.mapping.types.parameterizedTypes

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import java.lang.reflect.ParameterizedType
import kotlin.test.assertEquals

class A(private var foo: List<String>)

object O {
    @JvmStatic
    private var bar: List<String> = listOf()
}

fun topLevel(): List<String> = listOf()
fun Any.extension(): List<String> = listOf()

fun assertGenericType(type: KType) {
    val javaType = type.javaType
    if (javaType !is ParameterizedType) {
        throw AssertionError("Type should be a parameterized type, but was $javaType (${javaType.javaClass})")
    }
}

fun box(): String {
    val foo = A::class.java.kotlinClass.members.single { it.name == "foo" } as KMutableProperty<*>
    assertGenericType(foo.returnType)
    assertGenericType(foo.getter.returnType)
    assertGenericType(foo.setter.parameters.last().type)

    val bar = O::class.java.kotlinClass.members.single { it.name == "bar" } as KMutableProperty<*>
    assertGenericType(bar.returnType)
    assertGenericType(bar.getter.returnType)
    assertGenericType(bar.setter.parameters.last().type)

    val clazz = Class.forName("tests.mapping.types.parameterizedTypes.ParameterizedTypesKt").kotlinClass
    val topLevel = clazz.getMemberByName("topLevel")
    assertGenericType(topLevel.returnType)

    val extension = clazz.getMemberByName("extension")
    assertGenericType(extension.returnType)
    assertGenericType((A::class.java.kotlinClass as KClass<A>).constructors.first().parameters.single().type)
 
    return "OK"
}
