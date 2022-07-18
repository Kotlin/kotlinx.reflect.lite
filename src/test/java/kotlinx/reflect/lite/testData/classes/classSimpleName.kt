package tests.classes.classSimpleName

import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class Klass

fun box(): String {
    assertEquals("Klass", Klass::class.java.toLiteKClass().simpleName)
    // TODO ClassDescriptors for java classes are not supported
    // assertEquals("Date", java.util.Date::class.java.toLiteKClass().simpleName)
    //assertEquals("ObjectRef", kotlin.jvm.internal.Ref.ObjectRef::class.java.toLiteKClass().simpleName)
    return "OK"
}