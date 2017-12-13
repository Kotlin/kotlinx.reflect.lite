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
import kotlinx.reflect.lite.FunctionMetadata
import kotlinx.reflect.lite.ParameterMetadata
import kotlinx.reflect.lite.TypeMetadata
import org.jetbrains.kotlin.serialization.Flags
import org.jetbrains.kotlin.serialization.ProtoBuf
import org.jetbrains.kotlin.serialization.deserialization.NameResolver

internal class FunctionMetadataImpl(
        private val proto: ProtoBuf.Function,
        private val nameResolver: NameResolver
) : CallableMetadataImpl(), FunctionMetadata {
    override val name: String
        get() = nameResolver.getString(proto.name)

    override val parameters: List<ParameterMetadata>
        get() = proto.valueParameterList.map { ParameterMetadataImpl(it, nameResolver) }

    override val extensionReceiverType: TypeMetadata?
        get() = if (proto.hasReceiverType()) TypeMetadataImpl(proto.receiverType, nameResolver) else null

    override val returnType: TypeMetadata
        get() = TypeMetadataImpl(proto.returnType, nameResolver)

    override val visibility: DeclarationMetadata.Visibility?
        get() = Flags.VISIBILITY.get(proto.flags)?.toVisibility

    override val isInline: Boolean
        get() = Flags.IS_INLINE.get(proto.flags)

    override val isExternal: Boolean
        get() = Flags.IS_EXTERNAL_FUNCTION.get(proto.flags)

    override val isOperator: Boolean
        get() = Flags.IS_OPERATOR.get(proto.flags)

    override val isInfix: Boolean
        get() = Flags.IS_INFIX.get(proto.flags)

    override val isSuspend: Boolean
        get() = Flags.IS_SUSPEND.get(proto.flags)
}
