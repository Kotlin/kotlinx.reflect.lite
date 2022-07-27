package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.calls.*
import kotlinx.reflect.lite.calls.Caller
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.impl.KotlinReflectionInternalError
import kotlinx.reflect.lite.misc.JvmPropertySignature
import kotlinx.reflect.lite.name.*
import java.lang.reflect.*

internal class PropertyDescriptorImpl(
    val kmProperty: KmProperty,
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptor<*>?,
    override val container: ClassBasedDeclarationContainerDescriptor
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

    // TODO: also support overriden properties
    override val jvmSignature: JvmPropertySignature.KotlinProperty
        get() = JvmPropertySignature.KotlinProperty(
            this,
            kmProperty.fieldSignature,
            kmProperty.getterSignature,
            kmProperty.setterSignature
        )

    // Logic from: https://github.com/JetBrains/kotlin/blob/3b5179686eaba0a71bcca53c2cc922a54cc9241f/core/reflection.jvm/src/kotlin/reflect/jvm/internal/KPropertyImpl.kt#L51
    override val javaField: Field?
        get() = jvmSignature.fieldSignature?.let {
            // TODO: support propertyWithBackingFieldInOuterClass
            // TODO: support JavaField, JavaMethodProperty, MappedKotlinProperty
            val owner = containingClass?.jClass ?: container.jClass
            try {
                owner.getDeclaredField(it.name)
            } catch (e: NoSuchFieldException) {
                null
            }
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
    override val container: ClassBasedDeclarationContainerDescriptor
        get() = property.container

    override val typeParameterTable: TypeParameterTable
        get() = property.typeParameterTable
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
                        val javaField = property.javaField
                            ?: throw KotlinReflectionInternalError("No accessors or field is found for property $property")
                        computeFieldCaller(javaField)
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

    protected abstract fun computeFieldCaller(field: Field): Caller<*>

    protected fun isJvmStaticProperty(): Boolean {
        return false
        TODO("Implement isJvmStaticProperty: check annotations")
    }

    // Logic from: https://github.com/JetBrains/kotlin/blob/3b5179686eaba0a71bcca53c2cc922a54cc9241f/core/reflection.jvm/src/kotlin/reflect/jvm/internal/KPropertyImpl.kt#L321-L320
    protected fun PropertyDescriptor.isJvmFieldPropertyInCompanionObject(): Boolean {
        val container = containingClass
        if (container == null || !container.isCompanion) return false
        val outerClass = container.containingClass
        return when {
            // TODO: support isAnnotationClass, || outerClass?.isAnnotationClass == true
            outerClass?.isInterface == true  ->
                // TODO
                // this is DeserializedPropertyDescriptor && JvmProtoBufUtil.isMovedFromInterfaceCompanion(proto)
                false
            else -> true
        }
    }

    protected fun isNotNullProperty(): Boolean =
        !property.returnType.isNullableType()
}

internal class PropertyGetterDescriptorImpl(
    override val property: PropertyDescriptorImpl
) : PropertyAccessorDescriptorImpl(property), PropertyGetterDescriptor {
    override val name: Name
        get() = "<get-${property.name}>"

    override val flags: Flags
        get() = property.kmProperty.getterFlags

    override val valueParameters: List<ValueParameterDescriptor>
        get() = emptyList()

    override val member: Method?
        get() = property.jvmSignature.getterSignature?.let { signature ->
            property.container.findMethodBySignature(signature.name, signature.desc)
        }

    override fun computeFieldCaller(field: Field): Caller<*> = when {
        property.isJvmFieldPropertyInCompanionObject() || !Modifier.isStatic(field.modifiers) ->
            // TODO: bound receiver
            // if (isBound) CallerImpl.FieldGetter.BoundInstance(field, boundReceiver)
            CallerImpl.FieldGetter.Instance(field)
        isJvmStaticProperty() ->
            // TODO: bound receiver
            // if (isBound) CallerImpl.FieldGetter.BoundJvmStaticInObject(field)
            CallerImpl.FieldGetter.JvmStaticInObject(field)
        else ->
            CallerImpl.FieldGetter.Static(field)
    }
}

internal class PropertySetterDescriptorImpl(
    override val property: PropertyDescriptorImpl
) : PropertyAccessorDescriptorImpl(property), PropertySetterDescriptor {
    override val name: Name
        get() = "<set-${property.name}>"

    override val flags: Flags
        get() = property.kmProperty.setterFlags

    override val valueParameters: List<ValueParameterDescriptor>
        get() = listOf(PropertySetterParameterDescriptor(property.kmProperty.setterParameter, this))

    override val member: Method?
        get() = property.jvmSignature.setterSignature?.let { signature ->
            property.container.findMethodBySignature(signature.name, signature.desc)
        }

    override fun computeFieldCaller(field: Field): Caller<*> = when {
        property.isJvmFieldPropertyInCompanionObject() || !Modifier.isStatic(field.modifiers) ->
            // TODO: bound receiver
            // if (isBound) CallerImpl.FieldSetter.BoundInstance(field, isNotNullProperty(), boundReceiver)
            CallerImpl.FieldSetter.Instance(field, isNotNullProperty())
        isJvmStaticProperty() ->
            // TODO: bound receiver
            // if (isBound) CallerImpl.FieldSetter.BoundJvmStaticInObject(field, isNotNullProperty())
            CallerImpl.FieldSetter.JvmStaticInObject(field, isNotNullProperty())
        else ->
            CallerImpl.FieldSetter.Static(field, isNotNullProperty())
    }
}

// todo KProperty2Descriptor


