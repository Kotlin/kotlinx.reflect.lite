package tests.parameters.references

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

class C {
    fun foo() {}
    var bar = "OK"
}

fun C.extFun(i: Int) {}

fun KParameter.check(name: String?, kind: KParameter.Kind) {
    assertEquals(name, this.name)
    assertEquals(kind, this.kind)
}

fun box(): String {
    val cFoo = (C::class.java).kDeclarationContainer.getMemberByName("foo")
    val cBar = (C::class.java).kDeclarationContainer.getMemberByName("bar") as KMutableProperty1<C, String>
    val cExtFun = Class.forName("tests.parameters.references.ReferencesKt").kDeclarationContainer.getMemberByName("extFun")

    assertEquals(1, cFoo.parameters.size)
    assertEquals(1, cBar.getter.parameters.size)
    assertEquals(2, cBar.setter.parameters.size)

    assertEquals(2, cExtFun.parameters.size)
    cExtFun.parameters[0].check(null, KParameter.Kind.EXTENSION_RECEIVER)
    cExtFun.parameters[1].check("i", KParameter.Kind.VALUE)

    return "OK"
}
