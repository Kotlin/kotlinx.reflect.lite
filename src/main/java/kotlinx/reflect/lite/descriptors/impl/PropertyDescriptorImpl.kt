package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.calls.Caller
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.name.*
import java.lang.reflect.*

internal class PropertyDescriptorImpl(
    val kmProperty: KmProperty,
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptor<*>?,
    override val container: DeclarationContainerDescriptor
) : AbstractCallableDescriptor, PropertyDescriptor {
    override val flags: Flags
        get() = kmProperty.flags

    override val name: Name
        get() = kmProperty.name

    override val valueParameters: List<ValueParameterDescriptor>
        get() = emptyList()

    override val typeParameterTable: TypeParameterTable =
        kmProperty.typeParameters.toTypeParameters(this, module, containingClass?.typeParameterTable)

    override val typeParameters: List<TypeParameterDescriptor>
        get() = typeParameterTable.typeParameters

    override val returnType: KotlinType
        get() = kmProperty.returnType.toKotlinType(module, typeParameterTable)

    override val isConst: Boolean
        get() = Flag.Property.IS_CONST(flags)
    override val isLateInit: Boolean
        get() = Flag.Property.IS_LATEINIT(flags)
    override val isVar: Boolean
        get() = Flag.Property.IS_VAR(flags)
    override val isReal: Boolean
        get() = true

    override val dispatchReceiverParameter: ReceiverParameterDescriptor?
        get() = containingClass?.let { ReceiverParameterDescriptorImpl(it.defaultType, this) }

    override val extensionReceiverParameter: ReceiverParameterDescriptor?
        get() = kmProperty.receiverParameterType?.let { ReceiverParameterDescriptorImpl(it.toKotlinType(module, typeParameterTable), this) }

    override val getter: PropertyGetterDescriptor?
        get() = if (Flag.Property.HAS_GETTER(flags)) PropertyGetterDescriptorImpl(this) else null

    override val setter: PropertySetterDescriptor?
        get() = if (Flag.Property.HAS_SETTER(flags)) PropertySetterDescriptorImpl(this) else null

    override val caller: Caller<*>
        get() = getter?.caller ?: error("The property has no getter")

    override val defaultCaller: Caller<*>
        get() = getter?.defaultCaller ?: error("The property has no getter")
}

internal class PropertyGetterDescriptorImpl(
    override val property: PropertyDescriptorImpl
) : AbstractFunctionDescriptor(), PropertyGetterDescriptor {
    override val name: Name
        get() = "<get-${property.name}>"

    override val module: ModuleDescriptor
        get() = property.module
    override val containingClass: ClassDescriptor<*>?
        get() = property.containingClass
    override val container: DeclarationContainerDescriptor
        get() = property.container

    override val typeParameterTable: TypeParameterTable
        get() = property.typeParameterTable
    override val valueParameters: List<ValueParameterDescriptor>
        get() = emptyList()
    override val typeParameters: List<TypeParameterDescriptor>
        get() = property.typeParameters
    override val returnType: KotlinType
        get() = property.returnType

    override val dispatchReceiverParameter: ReceiverParameterDescriptor?
        get() = property.dispatchReceiverParameter
    override val extensionReceiverParameter: ReceiverParameterDescriptor?
        get() = property.extensionReceiverParameter

    override val isReal: Boolean
        get() = property.isReal

    override val flags: Flags
        get() = property.kmProperty.getterFlags

    private val jvmSignature: JvmFunctionSignature.KotlinFunction
        get() = JvmFunctionSignature.KotlinFunction(property.kmProperty.getterSignature ?: error("No getter signature for ${property.kmProperty}"))

    override val member: Member?
        get() = container.findMethodBySignature(jvmSignature.methodName, jvmSignature.methodDesc)

    override val defaultMember: Member?
        get() = container.findDefaultMethod(jvmSignature.methodName, jvmSignature.methodDesc, !Modifier.isStatic(caller.member!!.modifiers))
}

internal class PropertySetterDescriptorImpl(
    override val property: PropertyDescriptorImpl
) : AbstractFunctionDescriptor(), PropertySetterDescriptor {
    override val name: Name
        get() = "<set-${property.name}>"

    override val module: ModuleDescriptor
        get() = property.module
    override val containingClass: ClassDescriptor<*>?
        get() = property.containingClass
    override val container: DeclarationContainerDescriptor
        get() = property.container

    override val typeParameterTable: TypeParameterTable
        get() = property.typeParameterTable
    override val valueParameters: List<ValueParameterDescriptor> =
        listOf(ValueParameterDescriptorImpl(property.kmProperty.setterParameter!!, this, 0))
    override val typeParameters: List<TypeParameterDescriptor>
        get() = property.typeParameters
    override val returnType: KotlinType
        get() = module.findClass<Any?>("kotlin/Unit").kotlinType

    override val dispatchReceiverParameter: ReceiverParameterDescriptor?
        get() = property.dispatchReceiverParameter
    override val extensionReceiverParameter: ReceiverParameterDescriptor?
        get() = property.extensionReceiverParameter

    override val isReal: Boolean
        get() = property.isReal

    override val flags: Flags
        get() = property.kmProperty.setterFlags

    private val jvmSignature: JvmFunctionSignature.KotlinFunction
        get() = JvmFunctionSignature.KotlinFunction(property.kmProperty.getterSignature ?: error("No getter signature for ${property.kmProperty}"))

    override val member: Member?
        get() = container.findMethodBySignature(jvmSignature.methodName, jvmSignature.methodDesc)

    override val defaultMember: Member?
        get() = container.findDefaultMethod(jvmSignature.methodName, jvmSignature.methodDesc, !Modifier.isStatic(caller.member!!.modifiers))
}
