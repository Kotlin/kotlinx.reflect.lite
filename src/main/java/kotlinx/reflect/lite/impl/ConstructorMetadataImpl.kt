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

import kotlinx.reflect.lite.ConstructorMetadata
import kotlinx.reflect.lite.DeclarationMetadata
import kotlinx.reflect.lite.ParameterMetadata
import kotlinx.reflect.lite.TypeMetadata
import org.jetbrains.kotlin.serialization.Flags
import org.jetbrains.kotlin.serialization.ProtoBuf
import org.jetbrains.kotlin.serialization.deserialization.NameResolver

internal class ConstructorMetadataImpl(
        private val proto: ProtoBuf.Constructor,
        private val nameResolver: NameResolver
) : CallableMetadataImpl(), ConstructorMetadata {
    override val name: String
        get() = "<init>"

    override val parameters: List<ParameterMetadata>
        get() = proto.valueParameterList.map { ParameterMetadataImpl(it, nameResolver) }

    override val extensionReceiverType: TypeMetadata?
        get() = null

    override val isPrimary: Boolean
        get() = !Flags.IS_SECONDARY.get(proto.flags)

    override val visibility: DeclarationMetadata.Visibility?
        get() = Flags.VISIBILITY.get(proto.flags)?.toVisibility
}
