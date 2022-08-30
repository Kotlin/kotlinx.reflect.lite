/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.onTypes.arrayKClass

import kotlin.reflect.KClass

@Target(AnnotationTarget.TYPE)
annotation class MyAnn(val cls: KClass<*>)

val s: @MyAnn(Array<String>::class) String = ""

fun box(): String {
    val ann = ::s.returnType.annotations[0] as MyAnn
    return if (ann.cls == Array<String>::class) "OK" else "Fail: ${ann.cls}"
}
