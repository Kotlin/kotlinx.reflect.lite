/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */


package tests.methodsFromAny.classToString

import kotlin.test.*

class A {
    class Nested

    companion object
}

fun box(): String {
    assertEquals("class tests.methodsFromAny.classToString.A", "${A::class}")
    assertEquals("class tests.methodsFromAny.classToString.A\$Nested", "${A.Nested::class}")
    assertEquals("class tests.methodsFromAny.classToString.A\$Companion", "${A.Companion::class}")

    assertEquals("class kotlin.Any", "${Any::class}")
    assertEquals("class kotlin.Int", "${Int::class}")
    assertEquals("class kotlin.Int\$Companion", "${Int.Companion::class}")
    assertEquals("class kotlin.IntArray", "${IntArray::class}")
    assertEquals("class kotlin.String", "${String::class}")
    assertEquals("class kotlin.String", "${java.lang.String::class}")

    assertEquals("class kotlin.Array", "${Array<Any>::class}")
    assertEquals("class kotlin.Array", "${Array<Int>::class}")
    assertEquals("class kotlin.Array", "${Array<Array<String>>::class}")

    assertEquals("class java.lang.Runnable", "${Runnable::class}")

    return "OK"
}
