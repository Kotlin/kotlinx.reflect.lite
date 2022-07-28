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


}
