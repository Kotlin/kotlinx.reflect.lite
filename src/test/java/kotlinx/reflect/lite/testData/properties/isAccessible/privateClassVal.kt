package tests.properties.privateClassVal

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

class Result {
    private val value = "OK"

    fun ref() = (Result::class.java).kotlin.getMemberByName("value") as KProperty1<Result, String>
}

fun box(): String {
    val p = Result().ref()
    try {
        p.get(Result())
        return "Fail: private property is accessible by default"
    } catch(e: IllegalAccessException) {
        assertEquals("class kotlinx.reflect.lite.calls.CallerImpl\$FieldGetter cannot access a member of class tests.properties.privateClassVal.Result with modifiers \"private final\"", e.message)
    }

    p.isAccessible = true

    val r = p.get(Result())

    p.isAccessible = false
    try {
        p.get(Result())
        return "Fail: setAccessible(false) had no effect"
    } catch(e: IllegalAccessException) {
        assertEquals("class kotlinx.reflect.lite.calls.CallerImpl\$FieldGetter cannot access a member of class tests.properties.privateClassVal.Result with modifiers \"private final\"", e.message)
    }

    return r
}
