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
 * Provides access to the metadata of callable Kotlin declarations: functions, properties and constructors.
 */
interface CallableMetadata : DeclarationMetadata {
    /**
     * Name of this callable declaration, as declared in the source code. Constructors have the name "<init>".`
     */
    val name: String

    /**
     * Metadata for the parameters of this callable declaration, in the declaration order.
     * If this declaration is a property, an empty list is returned.
     */
    val parameters: List<ParameterMetadata>
}
