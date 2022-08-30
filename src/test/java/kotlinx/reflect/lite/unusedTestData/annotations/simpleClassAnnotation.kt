/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.simpleClassAnnotation

@Retention(AnnotationRetention.RUNTIME)
annotation class Simple(val value: String)

@Simple("OK")
class A

fun box(): String {
    return (A::class.annotations.single() as Simple).value
}
