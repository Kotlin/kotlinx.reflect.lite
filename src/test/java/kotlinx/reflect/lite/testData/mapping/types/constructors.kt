package tests.mapping.types.constructors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*

import kotlin.test.assertEquals

class A(d: Double, s: String, parent: A?) {
    class Nested(a: A)
    inner class Inner(nested: Nested)
}

enum class E(val i: Int) { ENTRY(1) }

fun box(): String {
    val aCons = (A::class.java.kotlin).constructors.first()
    assertEquals(listOf(java.lang.Double.TYPE, String::class.java, A::class.java), aCons.parameters.map { it.type.javaType })
    val aNestedCons = (A::class.java.kotlin).nestedClasses.single { it.simpleName == "Nested" }.constructors.first()
    assertEquals(listOf(A::class.java), aNestedCons.parameters.map { it.type.javaType })
    val aInnerCons = (A::class.java.kotlin).nestedClasses.single { it.simpleName == "Inner" }.constructors.first()
    assertEquals(listOf(A::class.java, A.Nested::class.java), aInnerCons.parameters.map { it.type.javaType })
    val eCons = (E::class.java.kotlin).constructors.single()
    assertEquals(listOf(java.lang.Integer.TYPE), eCons.parameters.map { it.type.javaType })

    assertEquals(A::class.java, aCons.returnType.javaType)
    assertEquals(A.Nested::class.java, aNestedCons.returnType.javaType)
    assertEquals(A.Inner::class.java, aInnerCons.returnType.javaType)

    return "OK"
}
