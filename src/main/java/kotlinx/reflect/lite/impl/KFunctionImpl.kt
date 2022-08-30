/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

internal class KFunctionImpl(
    override val descriptor: FunctionDescriptor
) : KCallableImpl<Any?>(), KFunction<Any?> {
    override val name: String
        get() = descriptor.name

    override val isInline: Boolean
        get() = descriptor.isInline
    override val isExternal: Boolean
        get() = descriptor.isExternal
    override val isOperator: Boolean
        get() = descriptor.isOperator
    override val isInfix: Boolean
        get() = descriptor.isInfix
    override val isSuspend: Boolean
        get() = descriptor.isSuspend

    override fun equals(other: Any?): Boolean =
        other is KFunctionImpl && descriptor == other.descriptor

    override fun hashCode(): Int = descriptor.hashCode()
}
