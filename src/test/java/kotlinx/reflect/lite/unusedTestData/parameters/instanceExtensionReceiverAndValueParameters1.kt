/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.instanceExtensionReceiverAndValueParameters1

import kotlin.reflect.full.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class A {
    fun String.memExt(param: Int) {}
}

fun topLevel() {}

fun Int.ext(vararg o: Any) {}

fun box(): String {
    A::class.members.single { it.name == "memExt" }.let {
        assertNotNull(it.instanceParameter)
        assertNotNull(it.extensionReceiverParameter)
        assertEquals(1, it.valueParameters.size)
    }

    ::topLevel.let {
        assertNull(it.instanceParameter)
        assertNull(it.extensionReceiverParameter)
        assertEquals(0, it.valueParameters.size)
    }

    Int::ext.let {
        assertNull(it.instanceParameter)
        assertNotNull(it.extensionReceiverParameter)
        assertEquals(1, it.valueParameters.size)
    }

    return "OK"
}
