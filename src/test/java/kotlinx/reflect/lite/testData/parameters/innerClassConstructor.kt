/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.innerClassConstructor

// Logic from this test, but inner class constructor has no bound receiver: https://github.com/Kotlin/kotlinx.reflect.lite/blob/mvicsokolova/dev/src/test/java/kotlinx/reflect/lite/unusedTestData/parameters/boundInnerClassConstructor.kt
// Bound receivers not supported for now

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertEquals

class Outer(val s1: String) {
    inner class Inner(val s2: String, val s3: String = "K") {
        val result = s1 + s2 + s3
    }
}

fun KParameter.check(name: String?, kind: KParameter.Kind) {
    assertEquals(name, this.name)
    assertEquals(kind, this.kind)
}

fun box(): String {
    val inner = ((Outer::class.java).kDeclarationContainer as KClass<Outer>).nestedClasses.single { it.simpleName == "Inner" }
    val ctor = inner.constructors.first()
    val ctorPararms = ctor.parameters

    ctorPararms[0].check(null, KParameter.Kind.INSTANCE)
    ctorPararms[1].check("s2", KParameter.Kind.VALUE)
    ctorPararms[2].check("s3", KParameter.Kind.VALUE)

    return "OK"
}
