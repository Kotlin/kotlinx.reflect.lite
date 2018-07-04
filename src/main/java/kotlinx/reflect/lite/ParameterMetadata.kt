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
 * Provides access to the metadata of a parameter of a Kotlin function or constructor.
 */
interface ParameterMetadata {
    /**
     * Name of this parameter as it was declared in the source code, or `null` if the parameter has no name or its name is not available at runtime.
     */
    val name: String?

    /**
     * Metadata for the type of this parameter.
     */
    val type: TypeMetadata

    /**
     * `true` if this parameter declares a default value in the source code.
     *
     * Note that in case the parameter inherits a default value from a super function, it's not considered to be declaring a default value.
     */
    val hasDefaultValue: Boolean
}
