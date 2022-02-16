package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.descriptors.ConstructorDescriptor
import kotlinx.reflect.lite.descriptors.ModuleDescriptor
import kotlinx.reflect.lite.descriptors.TypeParameterDescriptor
import kotlinx.reflect.lite.descriptors.ValueParameterDescriptor
import kotlinx.reflect.lite.descriptors.ValueParameterDescriptorImpl
import kotlinx.reflect.lite.name.*

internal class ConstructorDescriptorImpl(
    val kmCons: KmConstructor,
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptor<*>
) : AbstractFunctionDescriptor(), ConstructorDescriptor {
    override val flags: Flags
        get() = kmCons.flags

    override val name: Name
        get() = "<init>"

    override val valueParameters: List<ValueParameterDescriptor>
        get() = kmCons.valueParameters.mapIndexed { index, parameter ->
            ValueParameterDescriptorImpl(parameter, this, index)
        }

    override val typeParameterTable: TypeParameterTable
        get() = emptyList<KmTypeParameter>().toTypeParameters(this, module, containingClass.typeParameterTable)

    override val typeParameters: List<TypeParameterDescriptor>
        get() = emptyList()

    override val returnType: KotlinType
        get() = containingClass.kotlinType
}