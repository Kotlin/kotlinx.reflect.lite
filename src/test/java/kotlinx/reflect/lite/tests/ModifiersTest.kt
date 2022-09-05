/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.tests

import org.junit.*

class ModifiersTest {

    @Test
    fun testCallableModality() = test("modifiers.callableModality") { tests.modifiers.callableModality.box() }

    @Test
    fun testCallableVisibility() = test("modifiers.callableVisibility") { tests.modifiers.callableVisibility.box() }

    @Test
    fun testClassModality() = test("modifiers.classModality") { tests.modifiers.classModality.box() }

    @Test
    fun testClassVisibility() = test("modifiers.classVisibility") { tests.modifiers.classVisibility.box() }

    @Test
    fun testFunctions() = test("modifiers.functions") { tests.modifiers.functions.box() }

    @Test
    fun testProperties() = test("modifiers.properties") { tests.modifiers.properties.box() }

    @Test
    fun testTypeParameters() = test("modifiers.typeParameters") { tests.modifiers.typeParameters.box() }
}
