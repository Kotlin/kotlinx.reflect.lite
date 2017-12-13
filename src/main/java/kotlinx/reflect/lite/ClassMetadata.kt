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

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Provides access to the metadata of a Kotlin class, as well as to the metadata of individual members of this class.
 */
interface ClassMetadata : DeclarationMetadata {
    /**
     * Metadata for the given [method] if that method is visible from Kotlin source code and
     * belongs to this class, or `null` otherwise.
     */
    fun getFunction(method: Method): FunctionMetadata?

    /**
     * Metadata for the given [constructor] if that constructor is visible from Kotlin source code and
     * belongs to this class, or `null` otherwise.
     */
    fun getConstructor(constructor: Constructor<*>): ConstructorMetadata?

    /**
     * Metadata for the property, backed by the given [field], or `null` if there's no Kotlin property corresponding to that field.
     */
    fun getProperty(field: Field): PropertyMetadata?

    /**
     * `true` if this class is a data class.
     */
    val isData: Boolean

    /**
     * Kind of this class.
     */
    val kind: Kind

    /**
     * Represents different sorts of classes that can be declared in Kotlin code.
     */
    enum class Kind {
        /**
         * Class, declared simply with the keyword `class`.
         * Note that enum classes and annotation classes have separate kinds [ENUM_CLASS] and [ANNOTATION_CLASS].
         */
        CLASS,

        /**
         * Interface, declared with the keyword `interface`.
         */
        INTERFACE,

        /**
         * Enum class, declared with the keywords `enum class`.
         */
        ENUM_CLASS,

        /**
         * Annotation class, declared with the keywords `annotation class`.
         */
        ANNOTATION_CLASS,

        /**
         * Object, declared with the keyword `object`.
         * Note that companion objects have a separate kind [COMPANION_OBJECT].
         */
        OBJECT,

        /**
         * Companion object, declared with the keywords `companion object`.
         */
        COMPANION_OBJECT,
    }
}
