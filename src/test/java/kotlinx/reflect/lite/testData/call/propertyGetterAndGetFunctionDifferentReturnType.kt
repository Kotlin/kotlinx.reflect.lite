package tests.call.propertyGetterAndGetFunctionDifferentReturnType

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

data class Foo(val id: String) {
    fun getId() = -42 // Fail
}

fun box(): String {
    val id = Foo::class.java.kotlin.getMemberByName("id") as KProperty1<Foo, String>
    // TODO: `id.call(Foo("abs"))` fails with the error: "object is not an instance of declaring class", expected the same as `id.invoke(Foo("abc))`
    // id.call(Foo("abc"))
    id.invoke(Foo("dfdc"))
    return id.get(Foo("OK"))
}
