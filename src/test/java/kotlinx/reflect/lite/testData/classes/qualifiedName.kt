
package tests.classes.qualifiedName

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlin.test.assertEquals

class Klass {
    class Nested
    class `Nested$With$Dollars`
    companion object
}

fun box(): String {
    assertEquals("tests.classes.qualifiedName.Klass", Klass::class.java.kotlin.qualifiedName)
    assertEquals(
        "tests.classes.qualifiedName.Klass.Nested",
        Klass.Nested::class.java.kotlin.qualifiedName
    )
    assertEquals(
        "tests.classes.qualifiedName.Klass.Nested\$With\$Dollars",
        Klass.`Nested$With$Dollars`::class.java.kotlin.qualifiedName
    )
    assertEquals(
        "tests.classes.qualifiedName.Klass.Companion",
        Klass.Companion::class.java.kotlin.qualifiedName
    )

    assertEquals("java.util.Date", java.util.Date::class.java.kotlin.qualifiedName)
    assertEquals("kotlin.jvm.internal.Ref.ObjectRef", kotlin.jvm.internal.Ref.ObjectRef::class.java.kotlin.qualifiedName)

    class Local
    assertEquals(null, Local::class.java.kotlin.qualifiedName)

    val o = object {}
    assertEquals(null, o.javaClass.kotlin.qualifiedName)

    return "OK"
}
