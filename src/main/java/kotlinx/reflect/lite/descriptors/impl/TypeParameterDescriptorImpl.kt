package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.DeclarationDescriptor
import kotlinx.reflect.lite.descriptors.ModuleDescriptor
import kotlinx.reflect.lite.descriptors.TypeParameterDescriptor
import kotlinx.reflect.lite.name.*

internal class TypeParameterDescriptorImpl(
    private val kmTypeParam: KmTypeParameter,
    private val module: ModuleDescriptor,
    override val containingDeclaration: DeclarationDescriptor,
    private val typeParameterTable: TypeParameterTable
) : TypeParameterDescriptor {
    internal val id: Int get() = kmTypeParam.id

    override val name: Name
        get() = kmTypeParam.name

    override val isReified: Boolean
        get() = Flag.TypeParameter.IS_REIFIED(kmTypeParam.flags)

    override val upperBounds: List<KotlinType>
        get() = kmTypeParam.upperBounds.map { it.toKotlinType(module, typeParameterTable) }

    override val variance: KVariance
        get() = kmTypeParam.variance.toVariance()
}