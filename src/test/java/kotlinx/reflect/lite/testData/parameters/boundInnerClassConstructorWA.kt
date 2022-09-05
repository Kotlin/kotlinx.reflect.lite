/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.boundInnerClassConstructorWA

// Simi

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.full.*
import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertEquals

class Outer(val s1: String) {
    inner class Inner(val s2: String, val s3: String = "K") {
        val result = s1 + s2 + s3
    }
}

fun KParameter.check(name: String) {
    assertEquals(name, this.name!!)
    assertEquals(KParameter.Kind.VALUE, this.kind)
}

fun box(): String {
    val inner = Outer::class.java.kotlin.nestedClasses.single { it.simpleName == "Inner" } as KClass<Outer.Inner>
    val ctor = inner.primaryConstructor!!
    val ctorPararms = ctor.parameters

    ctorPararms[1].check("s2")
    ctorPararms[2].check("s3")

    return "OK"
}
