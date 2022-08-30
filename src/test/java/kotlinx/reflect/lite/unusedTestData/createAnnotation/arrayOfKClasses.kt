/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */


package tests.createAnnotation.arrayOfKClasses

import kotlin.reflect.KClass
import kotlin.test.assertEquals

annotation class Anno(val klasses: Array<KClass<*>> = arrayOf(String::class, Int::class))

fun box(): String {
    val anno = Anno::class.constructors.single().callBy(emptyMap())
    assertEquals(listOf(String::class, Int::class), anno.klasses.toList())
    assertEquals("@tests.createAnnotation.arrayOfKClasses.Anno(klasses=[class java.lang.String, int])", anno.toString())
    return "OK"
}
