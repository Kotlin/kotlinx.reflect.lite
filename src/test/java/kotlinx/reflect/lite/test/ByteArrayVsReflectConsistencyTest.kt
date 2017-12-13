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

import kotlinx.reflect.lite.ConstructorMetadata
import kotlinx.reflect.lite.FunctionMetadata
import kotlinx.reflect.lite.PropertyMetadata
import kotlinx.reflect.lite.ReflectionLite
import kotlinx.reflect.lite.impl.ClassMetadataImpl
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("unused")
class ByteArrayVsReflectConsistencyTest {
    @Test
    fun doTest() {
        val fromInputStream = ReflectionLite.loadClassMetadata(
                this::class.java.classLoader.getResourceAsStream("kotlinx/reflect/lite/impl/ClassMetadataImpl.class")
        )!!
        val fromClass = ReflectionLite.loadClassMetadata(ClassMetadataImpl::class.java)!!

        assertTrue(fromInputStream.functions.isNotEmpty())
        assertTrue(fromInputStream.constructors.isNotEmpty())
        assertTrue(fromInputStream.properties.isNotEmpty())

        assertEquals(fromClass.functions.map(FunctionMetadata::name).sorted(), fromInputStream.functions.map(FunctionMetadata::name).sorted())
        assertEquals(fromClass.constructors.map(ConstructorMetadata::name).sorted(), fromInputStream.constructors.map(ConstructorMetadata::name).sorted())
        assertEquals(fromClass.properties.map(PropertyMetadata::name).sorted(), fromInputStream.properties.map(PropertyMetadata::name).sorted())
    }
}
