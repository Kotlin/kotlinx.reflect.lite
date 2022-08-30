/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.tests

import org.junit.*

class KPropertyTest {
    // accessors
    @Test
    fun testMemberPropertyAccessors() = test("properties.accessors.memberPropertyAccessors") { tests.properties.accessors.memberPropertyAccessors.box() }

    @Test
    fun testExtensionPropertyAccessors() = test("properties.accessors.extensionPropertyAccessors") { tests.properties.accessors.extensionPropertyAccessors.box() }

    @Test
    fun testTopLevelPropertyAccessors() = test("properties.accessors.topLevelPropertyAccessors") { tests.properties.accessors.topLevelPropertyAccessors.box() }

    //--
    @Test
    fun testPropertyOfNestedClassAndArrayType() = test("properties.accessors.propertyOfNestedClassAndArrayType") { tests.properties.propertyOfNestedClassAndArrayType.box() }

    @Test
    fun testInvokeKProperty() = test("properties.accessors.invokeKProperty") { tests.properties.invokeKProperty.box() }

    @Test
    fun testSimpleGetProperties() = test("properties.accessors.simpleGetProperties") { tests.properties.simpleGetProperties.box() }

    // isAccessible
    @Test
    fun testAccessPrivateProperties() = test("properties.accessors.accessPrivateProperties") { tests.call.accessPrivateProperties.box() }

    @Test
    fun testPublicClassValAccessible() = test("properties.publicClassValAccessible") { tests.properties.publicClassValAccessible.box() }

    @Test
    fun testPrivateToThisAccessors() = test("properties.privateToThisAccessors") { tests.properties.privateToThisAccessors.box() }

    @Test
    fun testProtectedClassVar() = test("properties.protectedClassVar") { tests.properties.protectedClassVar.box() }

    @Test
    fun testPrivateClassVar() = test("properties.privateClassVar") { tests.properties.privateClassVar.box() }

    @Test
    fun testPrivatePropertyCallIsAccessibleOnAccessors() = test("properties.privatePropertyCallIsAccessibleOnAccessors") { tests.properties.privatePropertyCallIsAccessibleOnAccessors.box() }

    @Test
    fun testPrivateJvmStaticVarInObject() = test("properties.privateJvmStaticVarInObject") { tests.properties.privateJvmStaticVarInObject.box() }

    @Test
    fun testPrivateClassVal() = test("properties.privateClassVal") { tests.properties.privateClassVal.box() }

    @Test
    fun testCallPrivatePropertyFromGetProperties() = test("properties.callPrivatePropertyFromGetProperties") { tests.properties.callPrivatePropertyFromGetProperties.box() }
}
