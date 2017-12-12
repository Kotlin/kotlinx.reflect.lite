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
 * Provides access to the metadata related to an individual type usage in a Kotlin declaration.
 */
interface TypeMetadata {
    /**
     * `true` if this type was marked nullable in the source code.
     *
     * For Kotlin types, it means that `null` value is allowed to be represented by this type.
     * In practice it means that the type was declared with a question mark at the end.
     * For non-Kotlin types, it means the type or the symbol which was declared with this type
     * is annotated with a runtime-retained nullability annotation such as [javax.annotation.Nullable].
     *
     * Note that even if [isNullable] is false, values of the type can still be `null`.
     * This may happen if it is a type of the type parameter with a nullable upper bound:
     *
     * ```
     * fun <T> foo(t: T) {
     *     // isNullable == false for t's type, but t can be null here when T = "Any?"
     * }
     * ```
     */
    val isNullable: Boolean
}
