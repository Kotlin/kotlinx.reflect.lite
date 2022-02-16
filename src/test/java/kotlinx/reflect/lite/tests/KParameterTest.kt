package kotlinx.reflect.lite.tests

import org.junit.*

class KParameterTest {
    @Test
    fun testBigArity() = test("parameters.bigArity") { tests.parameters.bigArity.box() }

    @Test
    fun testBoundInnerClassConstructor() = test("parameters.boundInnerClassConstructor") { tests.parameters.boundInnerClassConstructor.box() }

    @Test
    fun testBoundObjectMemberReferences() = test("parameters.boundObjectMemberReferences") { tests.parameters.boundObjectMemberReferences.box() }

    @Test
    fun testBoundReferences() = test("parameters.boundReferences") { tests.parameters.boundReferences.box() }
}