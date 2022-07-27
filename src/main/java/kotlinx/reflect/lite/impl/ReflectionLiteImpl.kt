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

import kotlinx.metadata.*
import kotlinx.metadata.internal.common.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.builtins.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.name.*

internal object ReflectionLiteImpl {
    fun <T : Any> loadClassMetadata(jClass: Class<T>): KDeclarationContainer {
        return when (jClass.getAnnotation(Metadata::class.java)?.kind) {
            // if the class may be builtin or it's kind == CLASS_KIND -> try create a KClass
            // TODO: null case, check if is primitive or isArray
            null, 1 -> KClassImpl(ClassDescriptorImpl(jClass))
            // if it's kind == FILE_FACADE_KIND -> try create a KPackage
            // TODO: support header kinds MULTI_FILE_CLASS_FACADE_KIND, MULTI_FILE_CLASS_PART_KIND
            2 -> KPackageImpl(PackageDescriptorImpl(jClass))
            else -> throw KotlinReflectionInternalError("Can not load class metadata for $jClass")
        }
    }
}
