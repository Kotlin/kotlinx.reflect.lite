/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.jvmField.annotationCompanionWithAnnotation

import kotlin.reflect.full.declaredMemberProperties

annotation class Ann(val value: String)

public class Bar(public val value: String)

annotation class Foo {
    companion object {
        @JvmField @Ann("O")
        val FOO = Bar("K")
    }
}

fun box(): String {
    val field = Foo.Companion::class.declaredMemberProperties.single()
    return (field.annotations.single() as Ann).value + (field.get(Foo.Companion) as Bar).value
}
