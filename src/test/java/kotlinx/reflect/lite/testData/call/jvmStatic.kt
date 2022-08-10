package tests.call.jvmStatic

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*

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
    val foo = ((Obj::class.java).kDeclarationContainer as KClass<Obj>).getMemberByName("foo")
    foo.call(Obj)
    val bar = (C.Companion::class.java.kDeclarationContainer as KClass<C.Companion>).getMemberByName("bar")
    bar.call(C.Companion)
    return "OK"
}
