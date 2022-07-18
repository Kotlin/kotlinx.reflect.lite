
package tests.classes.qualifiedName

import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class Klass {
    class Nested
    class `Nested$With$Dollars`
    companion object
}

fun box(): String {
    assertEquals("tests.classes.qualifiedName.Klass", Klass::class.java.toLiteKClass().qualifiedName)
    assertEquals("tests.classes.qualifiedName.Klass.Nested", Klass.Nested::class.java.toLiteKClass().qualifiedName)
    assertEquals("tests.classes.qualifiedName.Klass.Nested\$With\$Dollars", Klass.`Nested$With$Dollars`::class.java.toLiteKClass().qualifiedName)
    assertEquals("tests.classes.qualifiedName.Klass.Companion", Klass.Companion::class.java.toLiteKClass().qualifiedName)
    // TODO ClassDescriptors for java classes are not supported
    //assertEquals("java.util.Date", java.util.Date::class.java.toLiteKClass().qualifiedName)
    //assertEquals("kotlin.jvm.internal.Ref.ObjectRef", kotlin.jvm.internal.Ref.ObjectRef::class.java.toLiteKClass().qualifiedName)

    class Local
    assertEquals(null, Local::class.java.toLiteKClass().qualifiedName)

    val o = object {}
    assertEquals(null, o.javaClass.toLiteKClass().qualifiedName)

    return "OK"
}