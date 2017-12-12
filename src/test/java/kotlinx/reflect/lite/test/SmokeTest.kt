/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kotlinx.reflect.lite.test

import kotlinx.reflect.lite.ReflectionLite
import org.junit.Test
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("UNUSED_PARAMETER", "unused")
class Subject(param: Int) {
    class Nested {
        fun method(nullableString: String?, nonNullIntArray: IntArray, nullableNested: Nested?): Int = 0
    }

    fun notNullListOfStrings(): List<String> = emptyList()
    fun nullableInt(): Int? = null

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

@Suppress("unused")
class SmokeTest {
    private fun Class<*>.methodByName(name: String): Method = declaredMethods.single { it.name == name }

    @Test
    fun testReturnTypeAndNullability() {
        val klass = Subject::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        val notNullListOfStrings = classMetadata.getFunction(klass.methodByName("notNullListOfStrings"))!!
        assertFalse(notNullListOfStrings.returnType.isNullable)

        val nullableInt = classMetadata.getFunction(klass.methodByName("nullableInt"))!!
        assertTrue(nullableInt.returnType.isNullable)
    }

    @Test
    fun testParameterNamesAndNullability() {
        val klass = Subject.Nested::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        val parameters = classMetadata.getFunction(klass.methodByName("method"))!!.parameters

        assertEquals(listOf(true, false, true), parameters.map { it.type.isNullable })
        assertEquals(listOf("nullableString", "nonNullIntArray", "nullableNested"), parameters.map { it.name })
    }

    @Test
    fun testConstructor() {
        val klass = Subject::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        val parameter = classMetadata.getConstructor(klass.declaredConstructors.single())!!.parameters.single()

        assertEquals("param", parameter.name)
        assertFalse(parameter.type.isNullable)
    }

    @Test
    fun testMappedTypes() {
        val klass = Subject::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        assertNotNull(classMetadata.getFunction(klass.methodByName("primitives")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("primitiveArrays")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("mappedCollections")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("mappedMutableCollections")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("mappedTypes")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("functionTypes")))
    }
}
