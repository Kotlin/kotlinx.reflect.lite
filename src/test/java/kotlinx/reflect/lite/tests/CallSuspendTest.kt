/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

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
