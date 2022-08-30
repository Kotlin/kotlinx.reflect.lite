/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.TypeParameterDescriptor

internal class KTypeParameterImpl(
    val descriptor: TypeParameterDescriptor
) : KTypeParameter {
    override val name: String
        get() = descriptor.name

    override val isReified: Boolean
        get() = descriptor.isReified

    override val upperBounds: List<KType>
        get() = descriptor.upperBounds.map(::KTypeImpl)

    override val variance: KVariance
        get() = descriptor.variance
}
