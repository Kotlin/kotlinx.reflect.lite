/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.lambdaClasses.reflectOnLambdaInArrayConstructor

import kotlin.reflect.jvm.*
import kotlin.test.assertNotNull

fun box(): String {
    assertNotNull({}.reflect())
    assertNotNull(Array(1) { {} }.single().reflect())

    return "OK"
}
