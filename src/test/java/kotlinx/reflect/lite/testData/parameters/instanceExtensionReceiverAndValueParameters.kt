package tests.parameters.instanceExtensionReceiverAndValueParameters

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
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
    (A::class.java).kotlinClass.getMemberByName("memExt").let {
        assertEquals(3, it.parameters.size)
        it.parameters[0].check(null, KParameter.Kind.INSTANCE)
        it.parameters[1].check(null, KParameter.Kind.EXTENSION_RECEIVER)
        it.parameters[2].check("param", KParameter.Kind.VALUE)
    }

    Class.forName("tests.parameters.instanceExtensionReceiverAndValueParameters.InstanceExtensionReceiverAndValueParametersKt")
        .kotlinClass.getMemberByName("topLevel").let {
            assertEquals(0, it.parameters.size)
        }

    Class.forName("tests.parameters.instanceExtensionReceiverAndValueParameters.InstanceExtensionReceiverAndValueParametersKt")
        .kotlinClass.getMemberByName("ext").let {
            assertEquals(2, it.parameters.size)
            it.parameters[0].check(null, KParameter.Kind.EXTENSION_RECEIVER)
            it.parameters[1].check("o", KParameter.Kind.VALUE)
        }

    return "OK"
}
