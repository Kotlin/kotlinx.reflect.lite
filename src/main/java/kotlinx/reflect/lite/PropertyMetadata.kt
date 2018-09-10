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

/**
 * Provides access to the metadata of a Kotlin property.
 */
interface PropertyMetadata : CallableMetadata {
    /**
     * Metadata for the type of values this property returns.
     */
    val returnType: TypeMetadata

    /**
     * `true` if this property is `lateinit`.
     */
    val isLateinit: Boolean

    /**
     * `true` if this property is `const`.
     */
    val isConst: Boolean
}
