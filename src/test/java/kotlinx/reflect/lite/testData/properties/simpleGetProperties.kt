package tests.properties.simpleGetProperties

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*

class A(param: String) {
    val int: Int get() = 42
    val string: String = param
    var anyVar: Any? = null

    val List<IntRange>.extensionToList: Unit get() {}

    fun notAProperty() {}
}

fun box(): String {
    val props = (A::class.java).kotlinClass.members.filterIsInstance<KProperty<*>>()

    val names = props.map { it.name }.sorted()
    assert(names == listOf("anyVar", "extensionToList", "int", "string")) { "Fail names: $props" }

    val stringProp = props.firstOrNull { it.name == "string" } as? KProperty1<A, String> ?: return "Fail, string not found: $props"
    return stringProp.get(A("OK")) as String
}
