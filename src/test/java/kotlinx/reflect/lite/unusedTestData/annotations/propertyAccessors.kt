/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.propertyAccessors

annotation class Get
annotation class Set
annotation class SetParam

var foo: String
    @Get get() = ""
    @Set set(@SetParam value) {}

fun box(): String {
    assert(::foo.getter.annotations.single() is Get)
    assert(::foo.setter.annotations.single() is Set)
    assert(::foo.setter.parameters.single().annotations.single() is SetParam)

    return "OK"
}
