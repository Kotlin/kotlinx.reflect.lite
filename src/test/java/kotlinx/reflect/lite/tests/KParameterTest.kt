/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.tests

import org.junit.*

class KParameterTest {
    @Test
    fun testBigArity() = test("parameters.bigArity") { tests.parameters.bigArity.box() }

    @Test
    fun testInnerClassConstructor() = test("parameters.innerClassConstructor") { tests.parameters.innerClassConstructor.box() }

    @Test
    fun testObjectMemberReferences() = test("parameters.objectMemberReferences") { tests.parameters.objectMemberReferences.box() }

    @Test
    fun testReferences() = test("parameters.references") { tests.parameters.references.box() }

    @Test
    fun testFunctionParameterNameAndIndex() = test("parameters.functionParameterNameAndIndex") { tests.parameters.functionParameterNameAndIndex.box() }

    @Test
    fun testInstanceExtensionReceiverAndValueParameters() = test("parameters.instanceExtensionReceiverAndValueParameters") { tests.parameters.instanceExtensionReceiverAndValueParameters.box() }

    @Test
    fun testInstanceParameterOfFakeOverride() = test("parameters.instanceParameterOfFakeOverride") { tests.parameters.instanceParameterOfFakeOverride.box() }

    @Test
    fun testIsMarkedNullable() = test("parameters.isMarkedNullable") { tests.parameters.isMarkedNullable.box() }

    @Test
    fun testIsOptional() = test("parameters.isOptional") { tests.parameters.isOptional.box() }

    @Test
    fun testKinds() = test("parameters.kinds") { tests.parameters.kinds.box() }

    @Test
    fun testPropertySetter() = test("parameters.propertySetter") { tests.parameters.propertySetter.box() }
}
