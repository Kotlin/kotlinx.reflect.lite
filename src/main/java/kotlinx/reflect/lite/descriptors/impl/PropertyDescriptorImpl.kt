package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.calls.*
import kotlinx.reflect.lite.calls.Caller
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.misc.JvmPropertySignature
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
        get() = containingClass?.let {
            ReceiverParameterDescriptorImpl(it.defaultType, this)
        }

    override val extensionReceiverParameter: ReceiverParameterDescriptor?
        get() = kmProperty.receiverParameterType?.let {
            ReceiverParameterDescriptorImpl(it.toKotlinType(module, typeParameterTable), this)
        }

    override val getter: PropertyGetterDescriptor?
        get() = if (Flag.Property.HAS_GETTER(flags)) PropertyGetterDescriptorImpl(this) else null

    override val setter: PropertySetterDescriptor?
        get() = if (Flag.Property.HAS_SETTER(flags)) PropertySetterDescriptorImpl(this) else null

    override val caller: Caller<*>
        get() = getter?.caller ?: error("The property has no getter")

    override val defaultCaller: Caller<*>
        get() = getter?.defaultCaller ?: error("The property has no getter")
}

internal abstract class PropertyAccessorDescriptorImpl(
    override val property: PropertyDescriptorImpl
) : AbstractFunctionDescriptor(), PropertyAccessorDescriptor {
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

    // TODO create abstract property jvmSignature
    // TODO abstract and override jvmSignature for PropertyAccessorDescriptor
    val jvmSignature: JvmPropertySignature.KotlinProperty
        get() = JvmPropertySignature.KotlinProperty(
            property,
            property.kmProperty.fieldSignature,
            property.kmProperty.getterSignature,
            property.kmProperty.setterSignature
        )

    abstract override val member: Method?

    override val defaultMember: Member?
        get() = null // TODO: no defaultMember for properties

    // TODO: support: JavaField, JavaMethodProperty, MappedKotlinProperty
    override val caller: Caller<*>
        get() {
            val accessor = member
            return when {
                accessor == null -> {
                    // todo inlineClassAwareCaller
                    if (property.isUnderlyingPropertyOfInlineClass() &&
                        property.visibility == KVisibility.INTERNAL
                    ) {
                        TODO("Inline class aware caller is not supported yet")
                    } else {
                        TODO("Implement PropertyDescriptor.javaField")
                    }
                }
                !Modifier.isStatic(accessor.modifiers) ->
                    // todo isBound
                    CallerImpl.Method.Instance(accessor)
                isJvmStaticProperty() ->
                    // todo isBound
                    CallerImpl.Method.JvmStaticInObject(accessor)
                else ->
                    // todo isBound
                    CallerImpl.Method.Static(accessor)
            }
        }

    fun isJvmStaticProperty(): Boolean {
        return TODO("Implement isJvmStaticProperty: check annotations")
    }
}

internal class PropertyGetterDescriptorImpl(
    override val property: PropertyDescriptorImpl
) : PropertyAccessorDescriptorImpl(property), PropertyGetterDescriptor {
    override val name: Name
        get() = "<get-${property.name}>"

    override val flags: Flags
        get() = property.kmProperty.getterFlags

    override val member: Method?
        get() = jvmSignature.getterSignature?.let { signature ->
            property.container.findMethodBySignature(signature.name, signature.desc)
        }
}

internal class PropertySetterDescriptorImpl(
    override val property: PropertyDescriptorImpl
) : PropertyAccessorDescriptorImpl(property), PropertySetterDescriptor {
    override val name: Name
        get() = "<set-${property.name}>"

    override val flags: Flags
        get() = property.kmProperty.setterFlags

    override val member: Method?
        get() = jvmSignature.setterSignature?.let { signature ->
            property.container.findMethodBySignature(signature.name, signature.desc)
        }
}

// todo KProperty2Descriptor


