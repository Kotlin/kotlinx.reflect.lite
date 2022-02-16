package tests.parameters.boundInnerClassConstructor

import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals
import kotlinx.reflect.lite.KParameter

class Outer(val s1: String) {
    inner class Inner(val s2: String, val s3: String = "K") {
        val result = s1 + s2 + s3
    }
}

fun KParameter.check(name: String) {
    assertEquals(name, this.name!!)
    assertEquals(KParameter.Kind.VALUE, this.kind)
}

fun box(): String {
    // TODO: metadata of Outer("O")::Inner is KotlinClassMetadata.Synthetic
    //val ctor = Outer("O")::Inner::class.java.toLiteKClass().constructors.first()

    // as KotlinClassMetadata.Class is only supported now, extract Inner via nestedClasses api
    val innerClass = Outer("O")::class.java.toLiteKClass().nestedClasses.first()
    assertEquals("Inner", innerClass.simpleName)
    val ctor = innerClass.constructors.first()
    val ctorPararms = ctor.parameters

    ctorPararms[0].check("s2")
    ctorPararms[1].check("s3")

    return "OK"
}