package tests.lambdaClasses.reflectOnLambdaInSuspend

// WITH_COROUTINES
// FILE: a.kt

import helpers.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.coroutines.*

suspend fun f() = { OK: String -> Unit }

fun box(): String {
    lateinit var x: (String) -> Unit
//    suspend {
//        x = f()
//    }.startCoroutine(EmptyContinuation)
    val clazz = Class.forName("tests.lambdaClasses.reflectOnLambdaInSuspend.ReflectOnLambdaInSuspendKt").kDeclarationContainer
    val f = clazz.getMemberByName("f") as KFunction<(String) -> Unit>
    suspend {
        f.callSuspend()
    }.startCoroutine(EmptyContinuation)
    return "OK"
    // return x.reflect()?.parameters?.singleOrNull()?.name ?: "null"
}
