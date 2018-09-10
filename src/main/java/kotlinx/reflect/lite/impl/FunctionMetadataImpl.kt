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
import kotlinx.metadata.jvm.JvmMethodSignature
import kotlinx.reflect.lite.DeclarationMetadata
import kotlinx.reflect.lite.FunctionMetadata
import kotlinx.reflect.lite.ParameterMetadata
import kotlinx.reflect.lite.TypeMetadata

internal class FunctionMetadataImpl(
    private val flags: Flags,
    override val name: String,
    override val extensionReceiverType: TypeMetadata?,
    override val parameters: List<ParameterMetadata>,
    override val returnType: TypeMetadata,
    val signature: JvmMethodSignature?
) : CallableMetadataImpl(), FunctionMetadata {
    override val visibility: DeclarationMetadata.Visibility?
        get() = flags.toVisibility

    override val isInline: Boolean
        get() = Flag.Function.IS_INLINE(flags)

    override val isExternal: Boolean
        get() = Flag.Function.IS_EXTERNAL(flags)

    override val isOperator: Boolean
        get() = Flag.Function.IS_OPERATOR(flags)

    override val isInfix: Boolean
        get() = Flag.Function.IS_INFIX(flags)

    override val isSuspend: Boolean
        get() = Flag.Function.IS_SUSPEND(flags)
}
