/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.createAnnotation.annotationType

import kotlin.test.assertEquals

annotation class Foo

fun box(): String {
    val foo = Foo::class.constructors.single().call()
    assertEquals(Foo::class, foo.annotationClass)
    return "OK"
}
