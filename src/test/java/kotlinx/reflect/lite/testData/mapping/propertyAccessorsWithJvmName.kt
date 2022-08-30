package tests.mapping.propertyAccessorsWithJvmName

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

var state: String = "value"
    @JvmName("getter")
    get
    @JvmName("setter")
    set

fun box(): String {
    val clazz = Class.forName("tests.mapping.propertyAccessorsWithJvmName.PropertyAccessorsWithJvmNameKt").kDeclarationContainer
    val p = clazz.getMemberByName("state") as KMutableProperty0<String>

    if (p.name != "state") return "Fail name: ${p.name}"
    if (p.get() != "value") return "Fail get: ${p.get()}"
    p.set("OK")

    val getterName = p.javaGetter!!.getName()
    if (getterName != "getter") return "Fail getter name: $getterName"

    val setterName = p.javaSetter!!.getName()
    if (setterName != "setter") return "Fail setter name: $setterName"

    return p.get()
}
