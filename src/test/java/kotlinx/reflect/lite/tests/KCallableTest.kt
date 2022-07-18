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
    fun testFakeOverride() = test("call.fakeOverride") { tests.call.fakeOverride.box() }

    @Test
    fun testFakeOverrideSubstituted() = test("call.fakeOverrideSubstituted") { tests.call.fakeOverrideSubstituted.box() }

    @Test
    fun testInnerClassConstructor() = test("call.innerClassConstructor") { tests.call.innerClassConstructor.box() }

    @Test
    fun testLocalClassMember() = test("call.localClassMember") { tests.call.localClassMember.box() }

    @Test
    fun testMemberOfGenericClass() = test("call.memberOfGenericClass") { tests.call.memberOfGenericClass.box() }

    @Test
    fun testSimpleTopLevelFunctions() = test("call.simpleTopLevelFunctions") { tests.call.simpleTopLevelFunctions.box() }

    // callBy
    @Test
    fun testSimpleMemberFunciton() = test("call.simpleMemberFunciton") { tests.callBy.simpleMemberFunciton.box() }
}
