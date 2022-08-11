package tests.callSuspend.callSuspend

// WITH_COROUTINES
// WITH_REFLECT
// TARGET_BACKEND: JVM

import helpers.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*

fun builder(c: suspend () -> Unit) {
    c.startCoroutine(EmptyContinuation)
}

class A {
    suspend fun noArgs() = "OK"

    suspend fun twoArgs(a: String, b: String) = "$a$b"
}

suspend fun twoArgs(a: String, b: String) = "$a$b"

fun ordinary() = "OK"

var log = ""

var proceed = {}

suspend fun suspendHere() = suspendCoroutineUninterceptedOrReturn<Unit> { cont ->
    proceed = {
        cont.resumeWith(Result.success(Unit))
    }
    COROUTINE_SUSPENDED
}

suspend fun suspending() {
    log += "before;"
    suspendHere()
    log += "after;"
}

fun box(): String {
    var res: String? = ""
    builder {
        res = (A::class.java).kotlin.members.find { it.name == "noArgs" }?.callSuspend(A()) as String?
    }
    if (res != "OK") return res ?: "FAIL 1"
    builder {
        res = (A::class.java).kotlin.members.find { it.name == "twoArgs" }?.callSuspend(A(), "O", "K") as String?
    }
    if (res != "OK") return res ?: "FAIL 2"
    builder {
        res = Class.forName("tests.callSuspend.callSuspend.CallSuspendKt").kotlinPackage.getMemberByName("twoArgs").callSuspend("O", "K") as String?
    }
    if (res != "OK") return res ?: "FAIL 3"
    builder {
        res = Class.forName("tests.callSuspend.callSuspend.CallSuspendKt").kotlinPackage.getMemberByName("ordinary").callSuspend() as String?
    }
    builder {
        Class.forName("tests.callSuspend.callSuspend.CallSuspendKt").kotlinPackage.getMemberByName("suspending").callSuspend()
    }
    log += "suspended;"
    proceed()
    if (log != "before;suspended;after;") return log
    return res ?: "FAIL 4"
}


