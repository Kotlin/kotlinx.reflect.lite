@file:Suppress("unused")

package kotlinx.reflect.lite.classloading

import helpers.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlin.coroutines.*
import kotlin.reflect.full.callSuspend as reflectCallSuspend
import kotlinx.reflect.lite.full.callSuspend as liteCallSuspend
import kotlinx.reflect.lite.jvm.kotlin as liteKotlin
import kotlin.jvm.kotlin as reflectKotlin

class Holder() {
    suspend fun suspendFoo() {}
    fun foo() {}
}


fun useRegularReflect() {
    // callBy
    val kClass = Holder::class.java.reflectKotlin
    val callable = kClass.members.single { it.name == "foo" }
    callable.callBy(mapOf(callable.parameters[0] to Holder()))

    // callBySuspend
    val callable2 = kClass.members.single { it.name == "suspendFoo" }
    val suspendCall = suspend { callable2.reflectCallSuspend(Holder()) }
    suspendCall.startCoroutine(EmptyContinuation)

    // Java
    SomeJavaClass::class.java.reflectKotlin

    // Introspection
    kClass.java
    for (member in kClass.members) {
        for (parameter in member.parameters) {
            parameter.toString()
            parameter.hashCode()
        }
        member.toString()
        member.hashCode()
    }
}

fun useReflectLite() {
    // callBy
    val kClass = Holder::class.java.liteKotlin
    val callable = kClass.members.single { it.name == "foo" }
    callable.callBy(mapOf(callable.parameters[0] to Holder()))

    // callBySuspend
    val callable2 = kClass.members.single { it.name == "suspendFoo" }
    val suspendCall = suspend { callable2.liteCallSuspend(Holder()) }
    suspendCall.startCoroutine(EmptyContinuation)

    // Java
    SomeJavaClass::class.java.liteKotlin

    // Introspection
    kClass.java
    for (member in kClass.members) {
        for (parameter in member.parameters) {
            parameter.toString()
            parameter.hashCode()
        }
        member.toString()
        member.hashCode()
    }

}

fun main() {
    // Do not run them both simultaneously
//    analyzeLoadedClasses("reflect", 8, 15) { useRegularReflect() }
    analyzeLoadedClasses("lite", 8, 15) { useReflectLite() }
}
