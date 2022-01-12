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

internal object ReflectionLiteImpl {
    fun loadClassMetadata(klass: Class<*>): KClass<*>? {
        val header = with(klass.getAnnotation(Metadata::class.java)) {
            KotlinClassHeader(kind, metadataVersion, data1, data2, extraString, packageName, extraInt)
        }

        val metadata = KotlinClassMetadata.read(header)
        if (metadata is KotlinClassMetadata.Class) {
            return KClassImpl<Any>(klass, metadata)
        }

        return null
    }
}