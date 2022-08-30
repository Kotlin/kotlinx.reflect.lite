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
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.descriptors.PackageDescriptor
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.misc.*

internal object ReflectionLiteImpl {
    fun <T : Any> createKotlinDeclarationContainer(jClass: Class<T>): KDeclarationContainer {
        return when (jClass.getMetadataAnnotation()?.kind) {
            null, KotlinClassHeader.CLASS_KIND -> createKotlinClass(jClass)
            KotlinClassHeader.FILE_FACADE_KIND, KotlinClassHeader.MULTI_FILE_CLASS_PART_KIND -> createKotlinPackage(jClass)
            else -> throw KotlinReflectionInternalError("Can not load class metadata for $jClass")
        }
    }

    fun <T: Any> createKotlinClass(jClass: Class<T>): KClass<T> {
        val kind = jClass.getMetadataAnnotation()?.kind
        return KClassImpl(createClassDescriptor(jClass, kind))
    }

    fun <T: Any> createKotlinPackage(jClass: Class<T>): KPackage<T> {
        val kind = jClass.getMetadataAnnotation()?.kind
        return KPackageImpl(createPackageDescriptor(jClass, kind))
    }

    private fun <T: Any> createClassDescriptor(jClass: Class<T>, kind: Int?): ClassDescriptor<T> =
        when (kind) {
            null -> {
                if (RuntimeTypeMapper.getKotlinBuiltInClassId(jClass) != null) {
                    ClassDescriptorImpl(jClass)
                } else {
                    JavaClassDescriptor(jClass)
                }
            }
            KotlinClassHeader.CLASS_KIND -> {
                ClassDescriptorImpl(jClass)
            }
            else -> throw KotlinReflectionInternalError("Could not create an instance of KClass from $this, the class file kind equals $kind")
        }

    private fun <T: Any> createPackageDescriptor(jClass: Class<T>, kind: Int?): PackageDescriptor<T> =
        when (kind) {
            // we can extract KmPackage only from these kinds of kotlin class files: FILE_FACADE_KIND, MULTI_FILE_CLASS_PART_KIND
            KotlinClassHeader.FILE_FACADE_KIND, KotlinClassHeader.MULTI_FILE_CLASS_PART_KIND -> PackageDescriptorImpl(jClass)
            else -> throw KotlinReflectionInternalError("Could not create an instance of KPackage from $jClass, the class file kind equals $kind")
        }

    private fun <T: Any> Class<T>.getMetadataAnnotation() = getAnnotation(Metadata::class.java)
}
