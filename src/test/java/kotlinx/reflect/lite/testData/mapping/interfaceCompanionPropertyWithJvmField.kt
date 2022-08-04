package tests.mapping.interfaceCompanionPropertyWithJvmField

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.reflect.jvm.*
import kotlin.test.*

interface Foo {
    companion object {
        @JvmField
        val value = "OK"
    }
}

fun box(): String {
    val value = Foo.Companion::class.java.kotlinClass.getMemberByName("value") as KProperty1<Foo.Companion, String>
    val field = value.javaField
    assertNotNull(field, "javaField not found for property declared in the interface's companion object")
    return field.get(null) as String
}
