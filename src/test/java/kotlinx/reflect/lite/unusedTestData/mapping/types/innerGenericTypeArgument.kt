/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.innerGenericTypeArgument

// "IOOBE: Invalid index 4, size is 4" for java.lang.reflect.ParameterizedType on Android


import kotlin.reflect.jvm.javaType
import kotlin.test.assertEquals

class Outer<A, B> {
    inner class Inner<C, D> {
        inner class Innermost<E, F>
    }
}

fun foo(): Outer<Int, Number>.Inner<String, Float>.Innermost<Any, Any?> = null!!

fun box(): String {
    assertEquals(
            listOf(
                    Any::class.java,
                    Any::class.java,
                    String::class.java,
                    Float::class.javaObjectType,
                    Int::class.javaObjectType,
                    Number::class.java
            ),
            ::foo.returnType.arguments.map { it.type!!.javaType }
    )

    return "OK"
}
