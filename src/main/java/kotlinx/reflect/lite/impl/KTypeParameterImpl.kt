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