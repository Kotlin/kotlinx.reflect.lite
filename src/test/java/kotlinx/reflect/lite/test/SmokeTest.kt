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

import kotlinx.reflect.lite.ClassMetadata
import kotlinx.reflect.lite.DeclarationMetadata
import kotlinx.reflect.lite.ReflectionLite
import org.junit.Test
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("unused")
class SmokeTest {
    private fun Class<*>.methodByName(name: String): Method = declaredMethods.single { it.name == name }

    private fun Class<*>.constructorBySignature(vararg paramTypes: Class<*>): Constructor<*> =
            declaredConstructors.single { it.parameterTypes.contentEquals(paramTypes) }

    @Test
    fun testReturnTypeAndNullability() {
        val klass = Types::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        val notNullListOfStrings = classMetadata.getFunction(klass.methodByName("notNullListOfStrings"))!!
        assertFalse(notNullListOfStrings.returnType.isNullable)

        val nullableInt = classMetadata.getFunction(klass.methodByName("nullableInt"))!!
        assertTrue(nullableInt.returnType.isNullable)
    }

    @Test
    fun testParameterNamesAndNullability() {
        val klass = Types.Nested::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        val parameters = classMetadata.getFunction(klass.methodByName("method"))!!.parameters

        assertEquals(listOf(true, false, true), parameters.map { it.type.isNullable })
        assertEquals(listOf("nullableString", "nonNullIntArray", "nullableNested"), parameters.map { it.name })
    }

    @Test
    fun testConstructor() {
        val klass = Types::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        val primaryConstructor = classMetadata.getConstructor(klass.constructorBySignature(Int::class.java))!!
        assertTrue(primaryConstructor.isPrimary)
        assertFalse(classMetadata.getConstructor(klass.constructorBySignature())!!.isPrimary)

        val parameter = primaryConstructor.parameters.single()
        assertEquals("param", parameter.name)
        assertFalse(parameter.type.isNullable)
    }

    @Test
    fun testMappedTypes() {
        val klass = Types::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        assertNotNull(classMetadata.getFunction(klass.methodByName("primitives")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("primitiveArrays")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("mappedCollections")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("mappedMutableCollections")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("mappedTypes")))
        assertNotNull(classMetadata.getFunction(klass.methodByName("functionTypes")))
    }

    @Test
    fun testDataClass() {
        assertTrue(ReflectionLite.loadClassMetadata(DataClass::class.java)!!.isData)
        assertFalse(ReflectionLite.loadClassMetadata(Types::class.java)!!.isData)
    }

    @Test
    fun testClassKinds() {
        assertEquals(ClassMetadata.Kind.CLASS, ReflectionLite.loadClassMetadata(ClassKinds.Class::class.java)!!.kind)
        assertEquals(ClassMetadata.Kind.INTERFACE, ReflectionLite.loadClassMetadata(ClassKinds.Interface::class.java)!!.kind)
        assertEquals(ClassMetadata.Kind.ENUM_CLASS, ReflectionLite.loadClassMetadata(ClassKinds.Enum::class.java)!!.kind)
        assertEquals(ClassMetadata.Kind.ANNOTATION_CLASS, ReflectionLite.loadClassMetadata(ClassKinds.Annotation::class.java)!!.kind)
        assertEquals(ClassMetadata.Kind.OBJECT, ReflectionLite.loadClassMetadata(ClassKinds.Object::class.java)!!.kind)
        assertEquals(ClassMetadata.Kind.COMPANION_OBJECT, ReflectionLite.loadClassMetadata(ClassKinds.Companion::class.java)!!.kind)

        assertEquals(ClassMetadata.Kind.CLASS, ReflectionLite.loadClassMetadata(ClassKinds.Enum.ENTRY::class.java)!!.kind)
    }

    @Test
    fun testVisibilities() {
        val klass = Visibilities::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!
        assertEquals(DeclarationMetadata.Visibility.PUBLIC, classMetadata.getFunction(klass.methodByName("publicFun"))!!.visibility)
        assertEquals(DeclarationMetadata.Visibility.PROTECTED, classMetadata.getFunction(klass.methodByName("protectedFun"))!!.visibility)
        assertEquals(DeclarationMetadata.Visibility.INTERNAL, classMetadata.getFunction(klass.methodByName("internalFun\$kotlinx_reflect_lite"))!!.visibility)
        assertEquals(DeclarationMetadata.Visibility.PRIVATE, classMetadata.getFunction(klass.methodByName("privateFun"))!!.visibility)

        assertEquals(DeclarationMetadata.Visibility.PUBLIC, classMetadata.getConstructor(klass.constructorBySignature())!!.visibility)
        assertEquals(DeclarationMetadata.Visibility.PROTECTED, classMetadata.getConstructor(klass.constructorBySignature(Int::class.java))!!.visibility)
        assertEquals(DeclarationMetadata.Visibility.INTERNAL, classMetadata.getConstructor(klass.constructorBySignature(Double::class.java))!!.visibility)
        assertEquals(DeclarationMetadata.Visibility.PRIVATE, classMetadata.getConstructor(klass.constructorBySignature(Float::class.java))!!.visibility)
    }
}
