/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.genericExtensionProperty

@Retention(AnnotationRetention.RUNTIME)
annotation class Simple(val value: String)

interface A<T>

@Simple("OK")
public val <T> A<T>.p: String
    get() = TODO()

fun box(): String {
    val o = object : A<Int> {}
    return (o::p.annotations.single() as Simple).value
}
