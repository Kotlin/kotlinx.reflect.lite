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
}