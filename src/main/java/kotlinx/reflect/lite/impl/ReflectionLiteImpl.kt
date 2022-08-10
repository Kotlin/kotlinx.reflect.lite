/*
 * Copyright 2010-2022 JetBrains s.r.o.
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

import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.impl.ReflectionLiteImpl.getMetadataAnnotation

internal object ReflectionLiteImpl {
    fun <T : Any> loadClassMetadata(jClass: Class<T>): KDeclarationContainer {
        return when (jClass.getMetadataAnnotation()?.kind) {
            // if metadata == null, jClass may be builtin -> try to create KClass
            null, 1 -> KClassImpl(ClassDescriptorImpl(jClass))
            2 -> KPackageImpl(PackageDescriptorImpl(jClass))
            else -> throw KotlinReflectionInternalError("Can not load class metadata for $jClass")
        }
    }

    fun <T: Any> createKotlinClass(jClass: Class<T>): KClass<T> {
        val metadata = jClass.getMetadataAnnotation()
        if (metadata == null || metadata.kind == KotlinClassHeader.CLASS_KIND) {
            return KClassImpl(ClassDescriptorImpl(jClass))
        } else {
            throw KotlinReflectionInternalError("Could not create an instance of KClass from $jClass, the class file kind equals ${metadata.kind}")
        }
    }

    fun <T: Any> createKotlinPackage(jClass: Class<T>): KPackage<T> {
        val metadata = jClass.getMetadataAnnotation()
        // we can extract KmPackage only from these kinds of kotlin class files: FILE_FACADE_KIND, MULTI_FILE_CLASS_PART_KIND
        if (metadata.kind == KotlinClassHeader.FILE_FACADE_KIND || metadata.kind == KotlinClassHeader.MULTI_FILE_CLASS_PART_KIND) {
            return KPackageImpl(PackageDescriptorImpl(jClass))
        } else {
            throw KotlinReflectionInternalError("Could not create an instance of KPackage from $jClass, the class file kind equals ${metadata.kind}")
        }
    }

    private fun <T: Any> Class<T>.getMetadataAnnotation() = getAnnotation(Metadata::class.java)
}
