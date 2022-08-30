package tests.mapping.types.typeParameters

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import java.lang.reflect.TypeVariable
import kotlin.test.assertEquals

class A<T : CharSequence> {
    fun foo(t: T) {}
}

fun box(): String {
    val f = A::class.java.kDeclarationContainer.getMemberByName("foo") as KFunction<Unit>
    val t = f.parameters.last().type.javaType
    if (t !is TypeVariable<*>) return "Fail, t should be a type variable: $t"

    assertEquals("T", t.name)
    assertEquals(A::class.java, (t.genericDeclaration as Class<*>))

    val tp = (A::class.java.kDeclarationContainer as KClass<A<*>>).typeParameters
    assertEquals(CharSequence::class.java, tp.single().upperBounds.single().javaType)

    return "OK"
}
