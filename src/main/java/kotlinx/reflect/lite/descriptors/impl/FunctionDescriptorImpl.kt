package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.descriptors.FunctionDescriptor
import kotlinx.reflect.lite.descriptors.ModuleDescriptor
import kotlinx.reflect.lite.descriptors.TypeParameterDescriptor
import kotlinx.reflect.lite.descriptors.ValueParameterDescriptor
import kotlinx.reflect.lite.descriptors.ValueParameterDescriptorImpl
import kotlinx.reflect.lite.name.*

abstract class AbstractFunctionDescriptor : AbstractCallableDescriptor(), FunctionDescriptor {
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

internal class FunctionDescriptorImpl(
    val kmFunction: KmFunction,
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptorImpl?
) : AbstractFunctionDescriptor() {
    override val flags: Flags
        get() = kmFunction.flags

    override val name: Name
        get() = kmFunction.name

    override val valueParameters: List<ValueParameterDescriptor>
        get() = kmFunction.valueParameters.mapIndexed { index, kmValueParam ->
            ValueParameterDescriptorImpl(kmValueParam, this, index)
        }

    override val typeParameterTable: TypeParameterTable =
        kmFunction.typeParameters.toTypeParameters(this, module, containingClass?.typeParameterTable)

    override val typeParameters: List<TypeParameterDescriptor>
        get() = typeParameterTable.typeParameters

    override val returnType: KotlinType
        get() = kmFunction.returnType.toKotlinType(module, typeParameterTable)
}