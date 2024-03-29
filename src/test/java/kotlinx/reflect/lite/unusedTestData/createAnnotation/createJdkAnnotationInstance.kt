/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.createAnnotation.createJdkAnnotationInstance

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.test.assertEquals

fun box(): String {
    val ctor = Retention::class.constructors.single()
    val r = ctor.callBy(mapOf(
            ctor.parameters.single { it.name == "value" } to RetentionPolicy.RUNTIME
    ))
    assertEquals(RetentionPolicy.RUNTIME, r.value as RetentionPolicy)
    assertEquals(Retention::class.java.classLoader, r.javaClass.classLoader)
    return "OK"
}
