package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

internal class KParameterImpl(
    val descriptor: ParameterDescriptor,
    override val index: Int,
    override val kind: KParameter.Kind
): KParameter {
    override val name: String
        get() = descriptor.name

    override val type: KType?
        get() = descriptor.type?.let(::KTypeImpl)

    override val isOptional: Boolean // TODO: inheritsDefaultValue
        get() = (descriptor as? ValueParameterDescriptor)?.declaresDefaultValue ?: false

    override val isVararg: Boolean
        get() = (descriptor is ValueParameterDescriptor) && descriptor.varargElementType != null
}