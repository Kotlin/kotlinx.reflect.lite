package tests.properties.publicClassValAccessible

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.full.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*


class Result {
    public val value: String = "OK"
}

fun box(): String {
    val p = (Result::class.java).kotlin.getMemberByName("value") as KProperty1<Result, String>
    p.isAccessible = false // should have no effect on the accessibility of a public reflection object
    return p.get(Result())
}
