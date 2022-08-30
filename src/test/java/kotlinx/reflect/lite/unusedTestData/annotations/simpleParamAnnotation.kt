/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.simpleParamAnnotation

@Retention(AnnotationRetention.RUNTIME)
annotation class Simple(val value: String)

fun test(@Simple("OK") x: Int) {}

fun box(): String {
    return (::test.parameters.single().annotations.single() as Simple).value
}
