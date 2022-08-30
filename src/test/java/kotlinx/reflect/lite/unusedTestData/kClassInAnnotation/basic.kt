/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.kClassInAnnotation.basic

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
annotation class Ann(val arg: KClass<*>)

class OK

@Ann(OK::class) class MyClass

fun box(): String {
    val argName = MyClass::class.java.getAnnotation(Ann::class.java).arg.simpleName ?: "fail 1"
    return argName
}
