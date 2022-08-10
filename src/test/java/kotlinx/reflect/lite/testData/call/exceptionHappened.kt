package tests.call.exceptionHappened

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import java.lang.reflect.InvocationTargetException

fun fail(message: String) {
    throw AssertionError(message)
}

fun box(): String {
    val clazz = Class.forName("tests.call.exceptionHappened.ExceptionHappenedKt").kDeclarationContainer
    val fail = clazz.getMemberByName("fail")
    try {
        fail.call("OK")
    } catch (e: InvocationTargetException) {
        return e.getTargetException().message.toString()
    }

    return "Fail: no exception was thrown"
}
