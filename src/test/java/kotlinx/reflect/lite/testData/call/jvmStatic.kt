package tests.call.jvmStatic

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*

object Obj {
    @JvmStatic
    fun foo() {}
}

class C {
    companion object {
        @JvmStatic
        fun bar() {}
    }
}

fun box(): String {
    val foo = (Obj::class.java).kotlin.getMemberByName("foo")
    foo.call(Obj)
    val bar = C.Companion::class.java.kotlin.getMemberByName("bar")
    bar.call(C.Companion)
    return "OK"
}
