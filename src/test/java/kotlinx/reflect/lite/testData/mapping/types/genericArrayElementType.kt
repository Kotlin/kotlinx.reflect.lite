/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.genericArrayElementType

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import java.lang.reflect.*
import kotlin.test.*

class Bar
fun arrayOfInvBar(): Array<Bar> = null!!
fun arrayOfInBar(): Array<in Bar> = null!!
fun arrayOfOutBar(): Array<out Bar> = null!!

fun arrayOfInvList(): Array<List<String>> = null!!
fun arrayOfInList(): Array<in List<String>> = null!!
fun arrayOfOutList(): Array<out List<String>> = null!!

fun box(): String {
    // NB: in "Array<in X>", Java type of X is always Any::class.java because this is the JVM signature generated by the compiler

    val clazz = Class.forName("tests.mapping.types.genericArrayElementType.GenericArrayElementTypeKt").kDeclarationContainer
    val arrayOfInvBar = clazz.getMemberByName("arrayOfInvBar") as KFunction<Array<Bar>>
    assertEquals(Bar::class.java, arrayOfInvBar.returnType.arguments.single().type!!.javaType)

    val arrayOfInBar = clazz.getMemberByName("arrayOfInBar") as KFunction<Array<in Bar>>
    assertEquals(Any::class.java, arrayOfInBar.returnType.arguments.single().type!!.javaType)

    val arrayOfOutBar = clazz.getMemberByName("arrayOfOutBar") as KFunction<Array<out Bar>>
    assertEquals(Bar::class.java, arrayOfOutBar.returnType.arguments.single().type!!.javaType)

    val arrayOfInvList = clazz.getMemberByName("arrayOfInvList") as KFunction<Array<List<String>>>
    val invList = arrayOfInvList.returnType.arguments.single().type!!.javaType
    assertTrue(invList is ParameterizedType && invList.rawType == List::class.java, invList.toString())

    val arrayOfInList = clazz.getMemberByName("arrayOfInList") as KFunction<Array<in List<String>>>
    assertEquals(Any::class.java, arrayOfInList.returnType.arguments.single().type!!.javaType)

    val arrayOfOutList = clazz.getMemberByName("arrayOfOutList") as KFunction<Array<out List<String>>>
    val outList = arrayOfOutList.returnType.arguments.single().type!!.javaType
    assertTrue(outList is ParameterizedType && outList.rawType == List::class.java, outList.toString())

    return "OK"
}
