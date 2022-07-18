package tests.call.exceptionHappened

import java.lang.reflect.InvocationTargetException

fun fail(message: String) {
    throw AssertionError(message)
}

// todo top-level functions

fun box(): String {
    try {
        ::fail.call("OK")
    } catch (e: InvocationTargetException) {
        return e.getTargetException().message.toString()
    }

    return "Fail: no exception was thrown"
}
