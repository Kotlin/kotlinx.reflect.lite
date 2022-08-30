/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.instanceExtensionReceiverAndValueParameters

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class A {
    fun String.memExt(param: Int) {}
}

fun topLevel() {}

fun Int.ext(vararg o: Any) {}

fun KParameter.check(name: String?, kind: KParameter.Kind) {
    assertEquals(name, this.name)
    assertEquals(kind, this.kind)
}

fun box(): String {
    (A::class.java).kDeclarationContainer.getMemberByName("memExt").let {
        assertEquals(3, it.parameters.size)
        it.parameters[0].check(null, KParameter.Kind.INSTANCE)
        it.parameters[1].check(null, KParameter.Kind.EXTENSION_RECEIVER)
        it.parameters[2].check("param", KParameter.Kind.VALUE)
    }

    Class.forName("tests.parameters.instanceExtensionReceiverAndValueParameters.InstanceExtensionReceiverAndValueParametersKt")
        .kDeclarationContainer.getMemberByName("topLevel").let {
            assertEquals(0, it.parameters.size)
        }

    Class.forName("tests.parameters.instanceExtensionReceiverAndValueParameters.InstanceExtensionReceiverAndValueParametersKt")
        .kDeclarationContainer.getMemberByName("ext").let {
            assertEquals(2, it.parameters.size)
            it.parameters[0].check(null, KParameter.Kind.EXTENSION_RECEIVER)
            it.parameters[1].check("o", KParameter.Kind.VALUE)
        }

    return "OK"
}
