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

package kotlinx.reflect.lite.impl

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.reflect.lite.DeclarationMetadata
import java.lang.reflect.Array

internal fun Class<*>.desc(): String {
    if (this == Void.TYPE) return "V"
    return Array.newInstance(this, 0).javaClass.name.substring(1).replace('.', '/')
}

internal val Flags.toVisibility: DeclarationMetadata.Visibility?
    get() = when {
        Flag.IS_PUBLIC(this) -> DeclarationMetadata.Visibility.PUBLIC
        Flag.IS_PROTECTED(this) -> DeclarationMetadata.Visibility.PROTECTED
        Flag.IS_INTERNAL(this) -> DeclarationMetadata.Visibility.INTERNAL
        Flag.IS_PRIVATE(this) || Flag.IS_PRIVATE_TO_THIS(this) -> DeclarationMetadata.Visibility.PRIVATE
        else -> null
    }

// To be able to pass `List::add0` as a callable reference to `also` (`List::add` only works with the new inference)
@Suppress("NOTHING_TO_INLINE")
inline fun <T> MutableList<T>.add0(element: T) {
    add(element)
}
