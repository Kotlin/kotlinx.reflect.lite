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

import kotlinx.reflect.lite.DeclarationMetadata
import org.jetbrains.kotlin.serialization.ProtoBuf
import java.lang.ref.SoftReference
import java.lang.reflect.Array
import kotlin.reflect.KProperty

internal fun <T> lazySoft(initializer: () -> T) = LazySoftImpl(initializer)

@Suppress("UNCHECKED_CAST")
internal class LazySoftImpl<out T>(private val initializer: () -> T) {
    @Volatile private var value: Any? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        (value as? SoftReference<T>)?.get()?.let { return it }

        return initializer().apply { value = SoftReference(this) }
    }
}

internal fun Class<*>.desc(): String {
    if (this == Void.TYPE) return "V"
    return Array.newInstance(this, 0).javaClass.name.substring(1).replace('.', '/')
}

internal val ProtoBuf.Visibility.toVisibility: DeclarationMetadata.Visibility?
    get() = when (this) {
        ProtoBuf.Visibility.PUBLIC -> DeclarationMetadata.Visibility.PUBLIC
        ProtoBuf.Visibility.PROTECTED -> DeclarationMetadata.Visibility.PROTECTED
        ProtoBuf.Visibility.INTERNAL -> DeclarationMetadata.Visibility.INTERNAL
        ProtoBuf.Visibility.PRIVATE, ProtoBuf.Visibility.PRIVATE_TO_THIS -> DeclarationMetadata.Visibility.PRIVATE
        else -> null
    }
