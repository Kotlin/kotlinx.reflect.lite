package tests.classes.classMembers

import kotlinx.reflect.lite.tests.*

open class Base {
    fun foo() {}

    val a = 20
}

class Derived : Base() {
    val b = 20

    fun bar() {}

    companion object {
        fun staticFun() {}
    }
}

fun Derived.extFun() {}

fun box(): String {
    Derived::class.java.toLiteKClass().members.forEach {
        println(it.name)
    }
    return "OK"
}

