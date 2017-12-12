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

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.ClassMetadata
import org.jetbrains.kotlin.serialization.jvm.JvmProtoBufUtil
import java.lang.ref.WeakReference
import java.util.*

internal object ReflectionLiteImpl {
    private val metadataFqName = "kotlin.Metadata"

    private val methods = WeakHashMap<Class<*>, WeakReference<MethodCache>>()

    @Suppress("UNCHECKED_CAST")
    private class MethodCache(klass: Class<*>) {
        val k = klass.getDeclaredMethod("k").let { method ->
            { instance: Annotation -> method(instance) as Int }
        }
        val d1 = klass.getDeclaredMethod("d1").let { method ->
            { instance: Annotation -> method(instance) as Array<String> }
        }
        val d2 = klass.getDeclaredMethod("d2").let { method ->
            { instance: Annotation -> method(instance) as Array<String> }
        }
    }

    fun loadClassMetadata(klass: Class<*>): ClassMetadata? {
        val annotation = klass.declaredAnnotations.singleOrNull { it.annotationClass.java.name == metadataFqName } ?: return null
        val metadataClass = annotation.annotationClass.java
        val methods = methods[metadataClass]?.get() ?: MethodCache(metadataClass).apply {
            methods[metadataClass] = WeakReference(this)
        }

        // Should be a class (kind = 1)
        if (methods.k(annotation) != 1) return null

        val (nameResolver, classProto) = JvmProtoBufUtil.readClassDataFrom(methods.d1(annotation), methods.d2(annotation))
        return ClassMetadataImpl(classProto, nameResolver)
    }
}
