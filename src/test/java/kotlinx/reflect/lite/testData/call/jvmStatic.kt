package tests.call.jvmStatic

import kotlinx.reflect.lite.tests.*

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
    val foo = (Obj::class.java).toLiteKClass().getMemberByName("foo")
    foo.call(Obj)
    val bar = C.Companion::class.java.toLiteKClass().getMemberByName("bar")
    bar.call(C.Companion)
    return "OK"
}
