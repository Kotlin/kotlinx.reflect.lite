// Part of the test: https://github.com/Kotlin/kotlinx.reflect.lite/blob/mvicsokolova/dev/src/test/java/kotlinx/reflect/lite/unusedTestData/mapping/methodsFromObject.kt

package tests.mapping.methodsFromObjectWithoutCustomMembers

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*

import kotlin.test.assertEquals

annotation class A
interface I
class C

interface MyCustomMembers {
    fun equals(): Boolean
    fun hashCode(hehe: Int): Int
    fun toString(hehe: String): Any
}

interface MyCloneable : Cloneable

fun KClass<*>.functions() = members.filterIsInstance<KFunction<*>>().map { it.javaMethod!!.name }.sorted()

fun box(): String {
    assertEquals(listOf("equals", "hashCode", "toString"), (A::class.java.kotlin).functions())
    assertEquals(listOf("equals", "hashCode", "toString"), (I::class.java.kotlin).functions())
    assertEquals(listOf("equals", "hashCode", "toString"), (C::class.java.kotlin).functions())

    // Now we can get only declared functions: equals(), hashCode(), toString()
//    assertEquals(
//        listOf("equals", "equals", "hashCode", "hashCode", "toString", "toString"),
//        (MyCustomMembers::class.java.kotlinClass as KClass<*>).functions()
//    )

    assertEquals(listOf("clone", "equals", "hashCode", "toString"), (MyCloneable::class.java.kotlin).functions())

    return "OK"
}
