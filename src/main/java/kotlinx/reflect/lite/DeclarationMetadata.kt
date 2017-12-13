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
 * Provides access to the metadata of any Kotlin declaration.
 */
interface DeclarationMetadata {
    /**
     * Visibility of this declaration, or `null` if the visibility cannot be represented in Kotlin.
     */
    val visibility: Visibility?

    /**
     * Represents different visibilities that Kotlin declarations can have.
     *
     * Note that some Java visibilities such as package-private and protected (which also gives access to items
     * from the same package) cannot be represented in Kotlin, so there's no [Visibility] value corresponding to them.
     */
    enum class Visibility {
        /**
         * Visibility of declarations marked with the `public` modifier, or with no modifier at all.
         */
        PUBLIC,

        /**
         * Visibility of declarations marked with the `protected` modifier.
         */
        PROTECTED,

        /**
         * Visibility of declarations marked with the `internal` modifier.
         */
        INTERNAL,

        /**
         * Visibility of declarations marked with the `private` modifier.
         */
        PRIVATE,
    }
}
