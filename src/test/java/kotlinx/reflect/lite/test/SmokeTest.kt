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

import kotlinx.reflect.lite.*
import org.junit.Test
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.test.*

@Suppress("unused")
class SmokeTest {
    private fun Class<*>.methodByName(name: String): Method = declaredMethods.single { it.name == name }

    private fun Class<*>.fieldByName(name: String): Field = declaredFields.single { it.name == name }

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

        val method = classMetadata.getFunction(klass.methodByName("method"))!!
        assertEquals("method", method.name)

        val parameters = method.parameters
        assertEquals(listOf(true, false, true), parameters.map { it.type.isNullable })
        assertEquals(listOf("nullableString", "nonNullIntArray", "nullableNested"), parameters.map { it.name })
    }

    @Test
    fun testConstructor() {
        val klass = Types::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        val primaryConstructor = classMetadata.getConstructor(klass.constructorBySignature(Int::class.java))!!
        assertTrue(primaryConstructor.isPrimary)
        val secondaryConstructor = classMetadata.getConstructor(klass.constructorBySignature())!!
        assertFalse(secondaryConstructor.isPrimary)

        assertEquals("<init>", primaryConstructor.name)
        assertEquals("<init>", secondaryConstructor.name)

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

    @Test
    fun testProperties() {
        val klass = Properties::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        val backingField = classMetadata.getProperty(klass.fieldByName("backingField"))!!
        assertEquals("backingField", backingField.name)
        assertTrue(backingField.returnType.isNullable)

        val delegated = classMetadata.getProperty(klass.fieldByName("delegated\$delegate"))!!
        assertEquals("delegated", delegated.name)
        assertFalse(delegated.returnType.isNullable)
    }

    @Test
    fun testEnumerateAll() {
        val klass = EnumerateAllCallables::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!
        assertEquals(listOf("function1", "function2"), classMetadata.functions.map(FunctionMetadata::name).sorted())
        assertEquals(listOf("property1", "property2"), classMetadata.properties.map(PropertyMetadata::name).sorted())
        assertEquals(2, classMetadata.constructors.size)
    }

    @Test
    fun testExtensionReceiverType() {
        val klass = ExtensionReceiverType::class.java
        val classMetadata = ReflectionLite.loadClassMetadata(klass)!!

        assertFalse(classMetadata.getFunction(klass.methodByName("stringExtFun"))!!.extensionReceiverType!!.isNullable)
        assertTrue(classMetadata.getFunction(klass.methodByName("nullableListExtFun"))!!.extensionReceiverType!!.isNullable)

        assertFalse(classMetadata.properties.single { it.name == "intExtProp" }.extensionReceiverType!!.isNullable)
        assertTrue(classMetadata.properties.single { it.name == "nullableDoubleExtProp" }.extensionReceiverType!!.isNullable)

        assertNull(classMetadata.getFunction(klass.methodByName("nonExtFun"))!!.extensionReceiverType)
        assertNull(classMetadata.constructors.single().extensionReceiverType)
    }
}
