package tests.mapping.types.memberFunctions

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class A {
    fun foo(t: Long?): Long = t!!
}

object O {
    @JvmStatic
    fun bar(a: A): String = ""
}

fun box(): String {
    val foo = A::class.java.kDeclarationContainer.getMemberByName("foo")
    assertEquals(listOf(A::class.java, java.lang.Long::class.java), foo.parameters.map { it.type.javaType })
    assertEquals(java.lang.Long.TYPE, foo.returnType.javaType)

    val bar = O::class.java.kDeclarationContainer.members.single { it.name == "bar" }
    assertEquals(listOf(O::class.java, A::class.java), bar.parameters.map { it.type.javaType })
    assertEquals(String::class.java, bar.returnType.javaType)

    return "OK"
}
