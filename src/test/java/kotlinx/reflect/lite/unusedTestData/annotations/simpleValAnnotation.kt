/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.simpleValAnnotation

@Retention(AnnotationRetention.RUNTIME)
annotation class Simple(val value: String)

@property:Simple("OK")
val foo: Int = 0

fun box(): String {
    return (::foo.annotations.single() as Simple).value
}
