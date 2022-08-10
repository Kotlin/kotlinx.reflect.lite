package tests.call.localClassMember

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*

fun box(): String {
    class Local {
        fun result(s: String) = s
    }

    val localCons = (Local::class.java).kotlin.getPrimaryConstructor()
    val result = (Local::class.java).kotlin.getMemberByName("result") as KFunction<String>
    return result.call(localCons.call(), "OK")
}
