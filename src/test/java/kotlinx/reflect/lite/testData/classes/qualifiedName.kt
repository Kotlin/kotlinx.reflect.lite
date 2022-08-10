
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
    assertEquals("tests.classes.qualifiedName.Klass", (Klass::class.java.kDeclarationContainer as KClass<Klass>).qualifiedName)
    assertEquals(
        "tests.classes.qualifiedName.Klass.Nested",
        (Klass.Nested::class.java.kDeclarationContainer as KClass<Klass.Nested>).qualifiedName
    )
    assertEquals(
        "tests.classes.qualifiedName.Klass.Nested\$With\$Dollars",
        (Klass.`Nested$With$Dollars`::class.java.kDeclarationContainer as KClass<Klass.`Nested$With$Dollars`>).qualifiedName
    )
    assertEquals(
        "tests.classes.qualifiedName.Klass.Companion",
        (Klass.Companion::class.java.kDeclarationContainer as KClass<Klass.Companion>).qualifiedName
    )
    // TODO ClassDescriptors for java classes are not supported
    //assertEquals("java.util.Date", java.util.Date::class.java.toLiteKClass().qualifiedName)
    //assertEquals("kotlin.jvm.internal.Ref.ObjectRef", kotlin.jvm.internal.Ref.ObjectRef::class.java.toLiteKClass().qualifiedName)

    class Local
    assertEquals(null, (Local::class.java.kDeclarationContainer as KClass<Local>).qualifiedName)

    val o = object {}
    assertEquals(null, (o.javaClass.kDeclarationContainer as KClass<Any>).qualifiedName)

    return "OK"
}
