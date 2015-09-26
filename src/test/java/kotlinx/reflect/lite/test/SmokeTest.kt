package kotlinx.reflect.lite.test

import kotlinx.reflect.lite.ReflectionLite
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class Subject(param: Int) {
    class Nested {
        fun method(nullableString: String?, nonNullIntArray: IntArray, nullableNested: Nested?): Int = 0
    }
}

class SmokeTest {
    @Test
    fun testParameterNamesAndNullability() {
        val klass = Subject.Nested::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass) ?: error("No class metadata found for $klass")

        val method = klass.getDeclaredMethod("method", String::class.java, IntArray::class.java, Subject.Nested::class.java)
        val methodMetadata = classMetadata.getFunction(method) ?: error("No function metadata found for $method")
        val parameters = methodMetadata.parameters

        assertEquals(listOf(true, false, true), parameters.map { it.type.isNullable })
        assertEquals(listOf("nullableString", "nonNullIntArray", "nullableNested"), parameters.map { it.name })
    }

    @Test
    fun testConstructor() {
        val klass = Subject::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass) ?: error("No class metadata found for $klass")

        val constructor = klass.declaredConstructors.single()
        val constructorMetadata = classMetadata.getConstructor(constructor) ?: error("No constructor metadata found for $constructor")
        val parameter = constructorMetadata.parameters.single()

        assertEquals("param", parameter.name)
        assertFalse(parameter.type.isNullable)
    }
}
