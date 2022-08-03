package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

internal class KParameterImpl(
    val descriptor: ParameterDescriptor,
    override val index: Int,
    override val kind: KParameter.Kind
): KParameter {
    override val name: String?
        get() = (descriptor as? ValueParameterDescriptor)?.let {
            if (it.name.startsWith("<")) null else it.name
        }

    override val type: KType
        get() = descriptor.type.let(::KTypeImpl)

    override val isOptional: Boolean
        get() = (descriptor as? ValueParameterDescriptor)?.declaresDefaultValue ?: false

    override val isVararg: Boolean
        get() = (descriptor is ValueParameterDescriptor) && descriptor.varargElementType != null

    override fun equals(other: Any?) =
        other is KParameterImpl &&
        descriptor.containingDeclaration == other.descriptor.containingDeclaration &&
        index == other.index

    override fun hashCode() =
        (descriptor.containingDeclaration.hashCode() * 31) + index.hashCode()
}
