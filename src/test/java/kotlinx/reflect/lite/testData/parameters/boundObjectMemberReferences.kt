package tests.parameters.boundObjectMemberReferences

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.reflect.*
import kotlin.reflect.jvm.*
import kotlin.test.*

object Host {
    fun foo(i: Int, s: String) {}
}

fun box(): String {
    val foo = Host::class.java.toLiteKClass().members.find { it.name == "foo" }
        ?: error("function foo is not found in the Host object")
    val fooParams = foo.parameters

    //assertEquals(2, fooParams.size)

    //assertEquals("i", fooParams[0].name)
    // TODO: javaType is not KType.javaType is not supported
    //assertEquals(Int::class.java, fooParams[0].type.javaType)

    assertEquals("i", fooParams[0].name)
    assertEquals("java.lang.Integer", (fooParams[0].type!!.classifier as KClassImpl<*>).qualifiedName)

    // TODO: javaType is not KType.javaType is not supported
    //assertEquals(String::class.java, fooParams[1].type.javaType)

    assertEquals("s", fooParams[1].name)
    assertEquals("java.lang.String", (fooParams[1].type!!.classifier as KClassImpl<*>).qualifiedName)

    return "OK"
}