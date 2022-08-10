package tests.properties.privateToThisAccessors

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.tests.*

class K<in T : String> {
    private var t: T
        get() = "OK" as T
        set(value) {}

    fun run(): String {
        val p = (K::class.java).kotlin.getMemberByName("t") as KMutableProperty1<K<String>, String>
        p.isAccessible = true
        p.set(this as K<String>, "")
        return p.get(this) as String
    }
}

fun box() = K<String>().run()
