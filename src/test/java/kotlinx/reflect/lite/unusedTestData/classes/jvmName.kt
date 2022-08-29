package tests.classes.jvmName

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.reflect.jvm.jvmName

class Klass {
    class Nested
    companion object
}

fun box(): String {
    assertEquals("tests.classes.jvmName.Klass", Klass::class.jvmName)
    assertEquals("tests.classes.jvmName.Klass\$Nested", Klass.Nested::class.jvmName)
    assertEquals("tests.classes.jvmName.Klass\$Companion", Klass.Companion::class.jvmName)

    class Local
    val l = Local::class.jvmName
    assertTrue(l != null && l.startsWith("tests.classes.jvmName.JvmNameKt\$") && "\$box\$" in l && l.endsWith("\$Local"))

    val obj = object {}
    val o = obj.javaClass.kotlin.jvmName
    assertTrue(o != null && o.startsWith("tests.classes.jvmName.JvmNameKt\$") && "\$box\$" in o && o.endsWith("\$1"))

    return "OK"
}
