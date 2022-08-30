package tests.properties.privateJvmStaticVarInObject

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.full.*
import kotlinx.reflect.lite.jvm.*

object Obj {
    @JvmStatic
    private var result: String = "Fail"
}

fun box(): String {
    val p = (Obj::class.java).kDeclarationContainer.members.single { it.name == "result" } as KMutableProperty1<Any?, String>
    p.isAccessible = true

    try {
        p.set(null, "OK")
        return "Fail: set should check that first argument is Obj"
    } catch (e: IllegalArgumentException) {}

    try {
        p.get(null)
        return "Fail: get should check that first argument is Obj"
    } catch (e: IllegalArgumentException) {}

    p.set(Obj, "OK")
    return p.get(Obj)
}
