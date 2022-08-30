/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.descriptors

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.descriptors.impl.ClassDescriptorImpl
import kotlinx.reflect.lite.impl.*
import java.lang.reflect.*

internal fun Flags.toVisibility(): KVisibility? =
    when {
        Flag.Common.IS_PRIVATE(this) -> KVisibility.PRIVATE
        Flag.Common.IS_PRIVATE_TO_THIS(this) -> KVisibility.PRIVATE
        Flag.Common.IS_INTERNAL(this) -> KVisibility.INTERNAL
        Flag.Common.IS_PROTECTED(this) -> KVisibility.PROTECTED
        Flag.Common.IS_PUBLIC(this) -> KVisibility.PUBLIC
        Flag.Common.IS_LOCAL(this) -> null
        else -> error("Declaration with unknown visibility")
    }

internal fun KmVariance.toVariance(): KVariance =
    when (this) {
        KmVariance.INVARIANT -> KVariance.INVARIANT
        KmVariance.IN -> KVariance.IN
        KmVariance.OUT -> KVariance.OUT
    }

internal fun Type.javaToKotlinType(module: ModuleDescriptor): KotlinType {
    return when (this) {
        is Class<*> -> KotlinType(
            module.findClass<Any?>(className) ?: TODO(className),
            emptyList(),
            false
        )
        else -> TODO("Unsupported Java type: $this (${this::class.java})")
    }
}

internal val Class<*>.className: ClassName
    get() = name.replace('.', '/').replace('$', '.')

