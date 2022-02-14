package tests.constructors.classesWithoutConstructors

import kotlinx.reflect.lite.tests.*
import kotlin.test.assertTrue

interface Interface
object Obj

class C {
    companion object
}

fun box(): String {
    assertTrue(Interface::class.java.toLiteKClass().constructors.isEmpty())
    assertTrue(Obj::class.java.toLiteKClass().constructors.isEmpty())
    assertTrue(C.Companion::class.java.toLiteKClass().constructors.isEmpty())
    assertTrue(object {}::class.java.toLiteKClass().constructors.isEmpty())

    return "OK"
}