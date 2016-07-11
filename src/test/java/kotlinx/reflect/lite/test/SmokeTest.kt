package kotlinx.reflect.lite.test

import kotlinx.reflect.lite.ReflectionLite
import org.junit.Test
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("UNUSED_PARAMETER")
class Subject(param: Int) {
    class Nested {
        fun method(nullableString: String?, nonNullIntArray: IntArray, nullableNested: Nested?): Int = 0
    }

    fun returnType(): List<String> = emptyList()
    fun nullableReturnType(): Int? = null
    fun primitives(z: Boolean, c: Char, b: Byte, s: Short, i: Int, f: Float, j: Long, d: Double) {}
    fun primitiveArrays(z: BooleanArray, c: CharArray, b: ByteArray, s: ShortArray, i: IntArray, f: FloatArray, j: LongArray, d: DoubleArray) {}
    fun mappedCollections(a: Iterable<*>, b: Iterator<*>, c: Collection<*>, d: List<*>, e: Set<*>, f: Map<*, *>, g: Map.Entry<*, *>, h: ListIterator<*>) {}
    fun mappedMutableCollections(
            a: MutableIterable<*>, b: MutableIterator<*>, c: MutableCollection<*>, d: MutableList<*>, e: MutableSet<*>,
            f: MutableMap<*, *>, g: MutableMap.MutableEntry<*, *>, h: MutableListIterator<*>
    ) {}
    fun mappedTypes(a: Any, b: String, c: CharSequence, d: Throwable, e: Cloneable, f: Number, g: Comparable<*>, h: Enum<*>, i: Annotation) {}
    fun functionTypes(a: () -> Unit, b: () -> String, c: (String) -> Unit, d: (String, Int, DoubleArray) -> List<*>,
                      e: (Any, Any, Any?, Array<Any?>, KClass<Any>, Class<Any>, List<Any>, Map<Any, Any>) -> Any?) {}
}

class SmokeTest {
    private fun Class<*>.methodByName(name: String): Method = declaredMethods.single { it.name == name }

    @Test
    fun testReturnTypeAndNullability() {
        val klass = Subject::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass) ?: error("No class metadata found for $klass")

        val method = klass.methodByName("returnType")
        val methodMetadata = classMetadata.getFunction(method) ?: error("No function metadata found for $method")
        val returnType = methodMetadata.returnType

        val nullableMethod = klass.methodByName("nullableReturnType")
        val nullableMethodMetadata = classMetadata.getFunction(nullableMethod) ?: error("No function metadata found for $nullableMethod")
        val nullableReturnType = nullableMethodMetadata.returnType

        assertFalse(returnType.isNullable)
        assertTrue(nullableReturnType.isNullable)
    }

    @Test
    fun testParameterNamesAndNullability() {
        val klass = Subject.Nested::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass) ?: error("No class metadata found for $klass")

        val method = klass.methodByName("method")
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

    @Test
    fun testMappedTypes() {
        val klass = Subject::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass) ?: error("No class metadata found for $klass")

        assertNotNull(classMetadata.getFunction(klass.methodByName("primitives")), "No function metadata for primitives")
        assertNotNull(classMetadata.getFunction(klass.methodByName("primitiveArrays")), "No function metadata for primitiveArrays")
        assertNotNull(classMetadata.getFunction(klass.methodByName("mappedCollections")), "No function metadata for mappedCollections")
        assertNotNull(classMetadata.getFunction(klass.methodByName("mappedMutableCollections")), "No function metadata for mappedMutableCollections")
        assertNotNull(classMetadata.getFunction(klass.methodByName("mappedTypes")), "No function metadata for mappedTypes")
        assertNotNull(classMetadata.getFunction(klass.methodByName("functionTypes")), "No function metadata for functionTypes")
    }
}
