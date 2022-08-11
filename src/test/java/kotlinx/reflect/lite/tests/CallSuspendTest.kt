package kotlinx.reflect.lite.tests

import org.junit.*

class CallSuspendTest {
    @Test
    fun testCallSuspend() = test("callSuspend.callSuspend") { tests.callSuspend.callSuspend.box() }

    @Test
    fun testCallSuspendBigArity() = test("callSuspend.bigArity") { tests.callSuspend.bigArity.box() }

    @Test
    fun testPrimitiveSuspendFunctions() = test("callSuspend.primitiveSuspendFunctions") { tests.callSuspend.primitiveSuspendFunctions.box() }
}
