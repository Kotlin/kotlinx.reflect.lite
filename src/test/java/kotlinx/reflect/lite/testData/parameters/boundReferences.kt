package tests.parameters.boundReferences

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

class C {
    fun foo() {}
    var bar = "OK"
}

fun C.extFun(i: Int) {}

fun KParameter.check(name: String) {
    assertEquals(name, this.name!!)
    assertEquals(KParameter.Kind.VALUE, this.kind)
}

fun box(): String {
    val kClass = C::class.java.toLiteKClass()
    val cFoo = kClass.members.find { it.name == "foo" } as KFunction
    val cBar = kClass.members.find { it.name == "bar" } as KProperty
    // TODO extension functions are not supported yet
    //val cExtFun = kClass.members.find { it.name == "extFun" } as KFunction

    assertEquals(1, cFoo.parameters.size)
    assertFalse(cBar.isConst)
    assertFalse(cBar.isLateinit)

    // TODO mutable properties are not supported yet
    //assertEquals(1, cBar.setter.parameters.size)

    //assertEquals(1, cExtFun.parameters.size)
    //cExtFun.parameters[0].check("i")

    return "OK"
}
