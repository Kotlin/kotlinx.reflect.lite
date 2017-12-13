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

package kotlinx.reflect.lite

import kotlinx.reflect.lite.impl.ReflectionLiteImpl
import java.io.InputStream

/**
 * The entry point to the lite reflection on Kotlin metadata.
 */
object ReflectionLite {
    /**
     * Metadata for the given [klass] if this is a Kotlin class, or `null` otherwise.
     */
    fun loadClassMetadata(klass: Class<*>): ClassMetadata? {
        return ReflectionLiteImpl.loadClassMetadata(klass)
    }

    /**
     * Metadata for the Kotlin class represented as a .class file that can be read from the given [inputStream],
     * or `null` if the .class file does not represent a Kotlin class.
     */
    fun loadClassMetadata(inputStream: InputStream): ClassMetadata? {
        return ReflectionLiteImpl.loadClassMetadata(inputStream)
    }
}
