package tests.constructors.classesWithoutConstructors

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertTrue

interface Interface
object Obj

class C {
    companion object
}

fun box(): String {
    assertTrue((Interface::class.java.kDeclarationContainer as KClass<Interface>).constructors.isEmpty())
    assertTrue((Obj::class.java.kDeclarationContainer as KClass<Obj>).constructors.isEmpty())
    assertTrue((C.Companion::class.java.kDeclarationContainer as KClass<C.Companion>).constructors.isEmpty())
    assertTrue(object {}::class.java.toLiteKClass().constructors.isEmpty())

    return "OK"
}
