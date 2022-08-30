package tests.properties.getPropertiesMutableVsReadonly

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.impl.*
import kotlin.reflect.*

class A(val readonly: String) {
    var mutable: String = "before"
}

fun box(): String {
    val props = (A::class.java).kotlin.members
    val readonly = props.single { it.name == "readonly" } as KProperty1<A, String>
    assert(readonly !is KMutableProperty1<A, *>) { "Fail 1: $readonly" }
    val mutable = props.single { it.name == "mutable" } as KMutableProperty1<A, String>
    assert(mutable is KMutableProperty1<A, *>) { "Fail 2: $mutable" }

    val a = A("")
    mutable as KMutableProperty1<A, String>
    assert(mutable.get(a) == "before") { "Fail 3: ${mutable.get(a)}" }
    mutable.set(a, "OK")
    return mutable.get(a)
}
