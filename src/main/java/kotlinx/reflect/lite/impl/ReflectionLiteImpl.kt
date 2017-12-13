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
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*

internal object ReflectionLiteImpl {
    private const val metadataFqName = "kotlin.Metadata"
    private const val metadataDesc = "Lkotlin/Metadata;"
    private const val metadataClassKind = "k"
    private const val metadataData = "d1"
    private const val metadataStrings = "d2"

    private val methods = WeakHashMap<Class<*>, WeakReference<MethodCache>>()

    @Suppress("UNCHECKED_CAST")
    private class MethodCache(klass: Class<*>) {
        val k = klass.getDeclaredMethod(metadataClassKind).let { method ->
            { instance: Annotation -> method(instance) as Int }
        }
        val d1 = klass.getDeclaredMethod(metadataData).let { method ->
            { instance: Annotation -> method(instance) as Array<String> }
        }
        val d2 = klass.getDeclaredMethod(metadataStrings).let { method ->
            { instance: Annotation -> method(instance) as Array<String> }
        }
    }

    fun loadClassMetadata(klass: Class<*>): ClassMetadata? {
        val annotation = klass.declaredAnnotations.singleOrNull { it.annotationClass.java.name == metadataFqName } ?: return null
        val metadataClass = annotation.annotationClass.java
        val methods = methods[metadataClass]?.get() ?: MethodCache(metadataClass).apply {
            methods[metadataClass] = WeakReference(this)
        }

        return loadClassMetadataImpl(methods.k(annotation), methods.d1(annotation), methods.d2(annotation))
    }

    fun loadClassMetadata(inputStream: InputStream): ClassMetadata? {
        var k: Int? = null
        var d1: Array<String>? = null
        var d2: Array<String>? = null
        ClassReader(inputStream).accept(object : ClassVisitor(Opcodes.ASM4) {
            override fun visitAnnotation(name: String, isVisible: Boolean): AnnotationVisitor? {
                if (name != metadataDesc) return null

                return object : AnnotationVisitor(Opcodes.ASM4) {
                    override fun visit(name: String, value: Any) {
                        if (name == metadataClassKind) {
                            k = value as? Int
                        }
                    }

                    override fun visitArray(name: String): AnnotationVisitor? {
                        if (name != metadataData && name != metadataStrings) return null

                        return object : AnnotationVisitor(Opcodes.ASM4) {
                            private val result = mutableListOf<String>()

                            override fun visit(name: String?, value: Any) {
                                (value as? String)?.let(result::add)
                            }

                            override fun visitEnd() {
                                when (name) {
                                    metadataData -> d1 = result.toTypedArray()
                                    metadataStrings -> d2 = result.toTypedArray()
                                }
                            }
                        }
                    }
                }
            }
        }, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)

        if (k == null || d1 == null || d2 == null) return null

        return ReflectionLiteImpl.loadClassMetadataImpl(k!!, d1!!, d2!!)
    }

    private fun loadClassMetadataImpl(k: Int, d1: Array<String>, d2: Array<String>): ClassMetadata? {
        // Should be a class (kind = 1)
        if (k != 1) return null

        val (nameResolver, classProto) = JvmProtoBufUtil.readClassDataFrom(d1, d2)
        return ClassMetadataImpl(classProto, nameResolver)
    }
}
