/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.tests

import org.junit.*

class KCallableTest {
    // call
    @Test
    fun testSimpleMemberFunction() = test("call.simpleMemberFunction") { tests.call.simpleMemberFunction.box() }

    @Test
    fun testSimpleConstructor() = test("call.simpleConstructor") { tests.call.simpleConstructor.box() }

    @Test
    fun testBigArity() = test("call.bigArity") { tests.call.bigArity.box() }

    @Test
    fun testCannotCallEnumConstructor() = test("call.cannotCallEnumConstructor") { tests.call.cannotCallEnumConstructor.box() }

    @Test
    fun testEqualsHashCodeToString() = test("call.equalsHashCodeToString") { tests.call.equalsHashCodeToString.box() }

    @Test
    fun testExceptionHappened() = test("call.exceptionHappened") { tests.call.exceptionHappened.box() }

    @Test
    fun testFakeOverride() = test("call.fakeOverride") { tests.call.fakeOverride.box() }

    @Test
    fun testFakeOverrideSubstituted() = test("call.fakeOverrideSubstituted") { tests.call.fakeOverrideSubstituted.box() }

    @Test
    fun testIncorrectNumberOfArguments() = test("call.incorrectNumberOfArguments") { tests.call.incorrectNumberOfArguments.box() }

    @Test
    fun testInnerClassConstructor() = test("call.innerClassConstructor") { tests.call.innerClassConstructor.box() }

    @Test
    fun testLocalClassMember() = test("call.localClassMember") { tests.call.localClassMember.box() }

    @Test
    fun testMemberOfGenericClass() = test("call.memberOfGenericClass") { tests.call.memberOfGenericClass.box() }

    @Test
    fun testSimpleTopLevelFunctions() = test("call.simpleTopLevelFunctions") { tests.call.simpleTopLevelFunctions.box() }

    @Test
    fun testJvmStatic() = test("call.jvmStatic") { tests.call.jvmStatic.box() }

    @Test
    fun testJvmStaticInObjectIncorrectReceiver() = test("call.jvmStaticInObjectIncorrectReceiver") { tests.call.jvmStaticInObjectIncorrectReceiver.box() }

    @Test
    fun testPropertyGetterAndGetFunctionDifferentReturnType() = test("call.propertyGetterAndGetFunctionDifferentReturnType") { tests.call.propertyGetterAndGetFunctionDifferentReturnType.box() }

    @Test
    fun testProtectedMembers() = test("call.protectedMembers") { tests.call.protectedMembers.box() }

    @Test
    fun testReturnUnit() = test("call.returnUnit") { tests.call.returnUnit.box() }

    // bound
    @Test
    fun testCompanionObjectPropertyAccessorsWA() = test("call.bound.companionObjectPropertyAccessorsWA") { tests.call.bound.companionObjectPropertyAccessorsWA.box() }

    @Test
    fun testExtensionPropertyAccessorsWA() = test("call.bound.extensionPropertyAccessorsWA") { tests.call.bound.extensionPropertyAccessorsWA.box() }

    @Test
    fun testJvmStaticCompanionObjectPropertyAccessorsWA() = test("call.bound.jvmStaticCompanionObjectPropertyAccessorsWA") { tests.call.bound.jvmStaticCompanionObjectPropertyAccessorsWA.box() }

    @Test
    fun testJvmStaticObjectPropertyAccessorsWA() = test("call.bound.jvmStaticObjectPropertyAccessorsWA") { tests.call.bound.jvmStaticObjectPropertyAccessorsWA.box() }

    @Test
    fun testMemberPropertyAccessorsWA() = test("call.bound.memberPropertyAccessorsWA") { tests.call.bound.memberPropertyAccessorsWA.box() }

    @Test
    fun testObjectPropertyAccessorsWA() = test("call.bound.objectPropertyAccessorsWA") { tests.call.bound.objectPropertyAccessorsWA.box() }

    @Test
    fun testExtensionFunctionWA() = test("call.bound.extensionFunctionWA") { tests.call.bound.extensionFunctionWA.box() }

    @Test
    fun testInnerClassConstructorWA() = test("call.bound.innerClassConstructorWA") { tests.call.bound.innerClassConstructorWA.box() }

    @Test
    fun testJvmStaticObjectFunctionWA() = test("call.bound.jvmStaticObjectFunctionWA") { tests.call.bound.jvmStaticObjectFunctionWA.box() }

    @Test
    fun testMemberFunctionWA() = test("call.bound.memberFunctionWA") { tests.call.bound.memberFunctionWA.box() }

    @Test
    fun testObjectFunctionWA() = test("call.bound.objectFunctionWA") { tests.call.bound.objectFunctionWA.box() }

    // callBy
    @Test
    fun testSimpleMemberFunciton() = test("callBy.simpleMemberFunciton") { tests.callBy.simpleMemberFunciton.box() }

    @Test
    fun testSimpleTopLevelFunction() = test("callBy.simpleTopLevelFunction") { tests.callBy.simpleTopLevelFunction.box() }

    @Test
    fun testSimpleConstructorCallBy() = test("callBy.simpleConstructor") { tests.callBy.simpleConstructor.box() }

    @Test
    fun testPrimitiveDefaultValuesCallBy() = test("callBy.primitiveDefaultValues") { tests.callBy.primitiveDefaultValues.box() }

    @Test
    fun testOrdinaryMethodIsInvokedWhenNoDefaultValuesAreUsed() = test("callBy.ordinaryMethodIsInvokedWhenNoDefaultValuesAreUsed") { tests.callBy.ordinaryMethodIsInvokedWhenNoDefaultValuesAreUsed.box() }

    @Test
    fun testNullValue() = test("callBy.nullValue") { tests.callBy.nullValue.box() }

    @Test
    fun testNonDefaultParameterOmitted() = test("callBy.nonDefaultParameterOmitted") { tests.callBy.nonDefaultParameterOmitted.box() }

    @Test
    fun testManyMaskArguments() = test("callBy.manyMaskArguments") { tests.callBy.manyMaskArguments.box() }

    @Test
    fun testDefaultAndNonDefaultIntertwined() = test("callBy.defaultAndNonDefaultIntertwined") { tests.callBy.defaultAndNonDefaultIntertwined.box() }

    @Test
    fun testManyArgumentsNoneDefaultFunction() = test("callBy.manyArgumentsNoneDefaultFunction") { tests.callBy.manyArgumentsNoneDefaultFunction.box() }

    @Test
    fun testManyArgumentsNoneDefaultConstructor() = test("callBy.manyArgumentsNoneDefaultConstructor") { tests.callBy.manyArgumentsNoneDefaultConstructor.box() }

    @Test
    fun testJvmStaticInObject() = test("callBy.jvmStaticInObject") { tests.callBy.jvmStaticInObject.box() }

    @Test
    fun testJvmStaticInCompanionObject() = test("callBy.jvmStaticInCompanionObject") { tests.callBy.jvmStaticInCompanionObject.box() }

    @Test
    fun testManyArgumentsOnlyOneDefault() = test("callBy.manyArgumentsOnlyOneDefault") { tests.callBy.manyArgumentsOnlyOneDefault.box() }

    @Test
    fun testExtensionFunction() = test("callBy.extensionFunction") { tests.callBy.extensionFunction.box() }

    @Test
    fun testBoundExtensionFunctionWA() = test("callBy.boundExtensionFunctionWA") { tests.callBy.boundExtensionFunctionWA.box() }

    @Test
    fun testBoundExtensionPropertyAcessorWA() = test("callBy.boundExtensionPropertyAcessorWA") { tests.callBy.boundExtensionPropertyAcessorWA.box() }

    @Test
    fun testBoundJvmStaticInObjectWA() = test("callBy.boundJvmStaticInObjectWA") { tests.callBy.boundJvmStaticInObjectWA.box() }

    @Test
    fun testCompanionObject() = test("callBy.companionObject") { tests.callBy.companionObject.box() }

    @Test
    fun testDefaultInSuperClassFakeOverride() = test("callBy.defaultInSuperClassFakeOverride") { tests.callBy.defaultInSuperClassFakeOverride.box() }

    @Test
    fun testDefaultInSuperInterface() = test("callBy.defaultInSuperInterface") { tests.callBy.defaultInSuperInterface.box() }
}
