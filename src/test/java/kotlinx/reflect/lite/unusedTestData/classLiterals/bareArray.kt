package tests.classLiterals.bareArray

import kotlin.test.*
import kotlin.reflect.KClass

fun box(): String {
    val any = Array<Any>::class
    val bare = Array::class

    assertEquals<KClass<*>>(any, bare)

    return "OK"
}