package tests.call.protectedMembers

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*

import kotlin.test.*

abstract class Base {
    protected val protectedVal: String
        get() = "1"

    var publicVarProtectedSet: String = ""
        protected set

    protected fun protectedFun(): String = "3"
}

class Derived : Base()

fun member(name: String): KCallable<*> =
    (Derived::class.java).kotlin.members.single { it.name == name }
        .apply { isAccessible = true }

fun box(): String {
    val a = Derived()

    assertEquals("1", (member("protectedVal") as KProperty1<Base, String>).invoke(a))

    val publicVarProtectedSet = member("publicVarProtectedSet") as KMutableProperty1<Derived, String>
    publicVarProtectedSet.setter.call(a, "2")
    assertEquals("2", publicVarProtectedSet.getter.call(a))
    assertEquals("2", publicVarProtectedSet.invoke(a))

    assertEquals("3", member("protectedFun").call(a))

    return "OK"
}
