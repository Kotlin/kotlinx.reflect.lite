/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.simpleConstructorAnnotation

annotation class Primary
annotation class Secondary

class C @Primary constructor() {
    @Secondary
    constructor(s: String): this()
}

fun box(): String {
    val ans = C::class.constructors.map { it.annotations.single().annotationClass.java.simpleName }.sorted()
    if (ans != listOf("Primary", "Secondary")) return "Fail: $ans"
    return "OK"
}
