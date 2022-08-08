package tests.classes.classSimpleName

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlin.test.assertEquals

class Klass

fun box(): String {
    assertEquals("Klass", (Klass::class.java.kotlinClass as KClass<Klass>).simpleName)
    // TODO ClassDescriptors for java classes are not supported
    // assertEquals("Date", java.util.Date::class.java.toLiteKClass().simpleName)
    //assertEquals("ObjectRef", kotlin.jvm.internal.Ref.ObjectRef::class.java.toLiteKClass().simpleName)
    return "OK"
}
