/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.parameterizedTypeArgument

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun listOfStrings(): List<String> = null!!

class Foo<T>
class Bar
fun fooOfInvBar(): Foo<Bar> = null!!
fun fooOfInBar(): Foo<in Bar> = null!!
fun fooOfOutBar(): Foo<out Bar> = null!!

fun box(): String {
    val clazz = Class.forName("tests.mapping.types.parameterizedTypeArgument.ParameterizedTypeArgumentKt").kDeclarationContainer
    val listOfStrings = clazz.getMemberByName("listOfStrings") as KFunction<List<String>>
    assertEquals(String::class.java, listOfStrings.returnType.arguments.single().type!!.javaType)

    val fooOfInvBar = clazz.getMemberByName("fooOfInvBar") as KFunction<Foo<Bar>>
    assertEquals(Bar::class.java, fooOfInvBar.returnType.arguments.single().type!!.javaType)

    val fooOfInBar = clazz.getMemberByName("fooOfInBar") as KFunction<Foo<in Bar>>
    assertEquals(Bar::class.java, fooOfInBar.returnType.arguments.single().type!!.javaType)

    val fooOfOutBar = clazz.getMemberByName("fooOfOutBar") as KFunction<Foo<out Bar>>
    assertEquals(Bar::class.java, fooOfOutBar.returnType.arguments.single().type!!.javaType)

    return "OK"
}
