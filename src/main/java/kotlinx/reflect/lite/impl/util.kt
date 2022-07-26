package kotlinx.reflect.lite.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.KVisibility
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.descriptors.impl.KotlinType
import kotlinx.reflect.lite.calls.*
import kotlinx.reflect.lite.misc.JvmPropertySignature
import java.lang.reflect.*

internal fun createKCallable(descriptor: CallableDescriptor): KCallableImpl<*> {
    if (descriptor is PropertyDescriptor) {
        val receiverCount = (descriptor.dispatchReceiverParameter?.let { 1 } ?: 0) +
                (descriptor.extensionReceiverParameter?.let { 1 } ?: 0)

        when {
            descriptor.isVar ->
                when (receiverCount) {
                    0 -> return KMutableProperty0Impl<Any?>(descriptor)
                    1 -> return KMutableProperty1Impl<Any?, Any?>(descriptor)
                    else -> TODO("Implement mutable properties for other numbers of receivers")
//                2 -> return KMutableProperty2Impl<Any?, Any?, Any?>(container, descriptor)
            }
            else -> when (receiverCount) {
                0 -> return KProperty0Impl<Any?>(descriptor)
                1 -> return KProperty1Impl<Any?, Any?>(descriptor)
                else -> TODO("Implement properties for other numbers of receivers")
//                2 -> return KProperty2Impl<Any?, Any?, Any?>(container, descriptor)
            }
        }
    }
    if (descriptor is FunctionDescriptor) {
        return KFunctionImpl(descriptor)
    }
    throw KotlinReflectionInternalError("Unsupported callable: $descriptor")
}

internal fun <D : CallableDescriptor> overrides(derived: D, base: D): Boolean {
    require(derived.name == base.name) { "Names should be equal: $derived, $base" }
    if (derived is PropertyDescriptor && base is PropertyDescriptor) {
        // TODO (!)
        return true
    } else if (derived is FunctionDescriptor && base is FunctionDescriptor) {
        // TODO (!)
        return true
    } else {
        error("Unknown members: $derived, $base")
    }
}

internal fun <D : CallableDescriptor> addFakeOverrides(
    klass: ClassDescriptor<*>,
    realMembers: List<D>,
    selector: (ClassDescriptor<*>) -> List<D>,
    createFakeOverride: (List<D>) -> D
): List<D> {
    val fromDerived = realMembers.groupBy(CallableDescriptor::name)

    val fromBase = mutableMapOf<String, MutableList<D>>()
    for (supertype in klass.supertypes) {
        val superclass = supertype.descriptor as? ClassDescriptor<*> ?: continue
        val allMembers = selector(superclass).filter { it.visibility != KVisibility.PRIVATE }
        for ((name, members) in allMembers.groupBy(CallableDescriptor::name)) {
            fromBase.getOrPut(name) { ArrayList(1) }.addAll(members)
        }
    }

    val result = mutableListOf<D>()
    for ((name, members) in fromBase) {
        val notOverridden = members.filterNot { baseMember ->
            fromDerived[name].orEmpty().any { derivedMember -> overrides(derivedMember, baseMember)}
        }
        if (notOverridden.isEmpty()) continue
        // TODO (!): if > 1, group by extension receiver and parameter types
        // TODO: filterOutOverridden (so that `isAbstract = notOverridden.all { it.isAbstract }` would work)
        result.add(createFakeOverride(notOverridden))
    }

    return result
}

internal fun addPropertyFakeOverrides(klass: ClassDescriptor<*>, realProperties: List<PropertyDescriptor>): List<PropertyDescriptor> =
    addFakeOverrides(klass, realProperties, { it.memberScope.properties }) { members ->
        val representative = members.first()
        // TODO (!): type substitution
        FakeOverridePropertyDescriptor(
            klass.module,
            klass,
            null, // todo: representative.extensionReceiverParameter
            representative.valueParameters,
            representative.typeParameters,
            representative.returnType,
            members
        )
    }

internal fun addFunctionFakeOverrides(klass: ClassDescriptor<*>, realFunctions: List<FunctionDescriptor>): List<FunctionDescriptor> =
    addFakeOverrides(klass, realFunctions, { it.memberScope.functions }) { members ->
        // TODO (!): type substitution
        val representative = members.first()
        FakeOverrideFunctionDescriptor(
            klass.module,
            klass,
            null, // todo: representative.extensionReceiverParameter
            representative.valueParameters,
            representative.typeParameters,
            representative.returnType,
            members
        )
    }

internal abstract class FakeOverrideCallableMemberDescriptor(
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptor<*>,
    override val extensionReceiverParameter: ReceiverParameterDescriptor?,
    override val valueParameters: List<ValueParameterDescriptor>,
    override val typeParameters: List<TypeParameterDescriptor>,
    override val returnType: KotlinType,
    val overridden: List<CallableDescriptor>
) : CallableDescriptor, AbstractFunctionDescriptor() {
    init {
        require(overridden.isNotEmpty())
    }

    override val flags: Flags
        get() = TODO("Not yet implemented")

    override val name: String
        get() = overridden.first().name
    override val visibility: KVisibility?
        get() = overridden.first().visibility

    override val container: ClassBasedDeclarationContainerDescriptor
        get() = containingClass

    override val typeParameterTable: TypeParameterTable
        get() = TODO("Not yet implemented")

    override val dispatchReceiverParameter: ReceiverParameterDescriptor?
        get() = ReceiverParameterDescriptorImpl(containingClass.defaultType, this)

    override val isFinal: Boolean
        get() = overridden.any(CallableDescriptor::isFinal)
    override val isAbstract: Boolean
        get() = overridden.all(CallableDescriptor::isAbstract)
    override val isOpen: Boolean
        get() = !isFinal && !isAbstract

    override val isReal: Boolean
        get() = false
}

internal class FakeOverrideFunctionDescriptor(
    module: ModuleDescriptor,
    containingClass: ClassDescriptor<*>,
    extensionReceiverParameter: ReceiverParameterDescriptor?,
    valueParameters: List<ValueParameterDescriptor>,
    typeParameters: List<TypeParameterDescriptor>,
    returnType: KotlinType,
    overridden: List<FunctionDescriptor>
) : FakeOverrideCallableMemberDescriptor(
    module, containingClass, extensionReceiverParameter, valueParameters, typeParameters, returnType, overridden
) {
    @Suppress("UNCHECKED_CAST")
    val overriddenFunctions: List<FunctionDescriptor>
        get() = super.overridden as List<FunctionDescriptor>

    override val isInline: Boolean
        get() = overriddenFunctions.first().isInline // TODO?
    override val isOperator: Boolean
        get() = overriddenFunctions.first().isOperator // TODO?
    override val isInfix: Boolean
        get() = overriddenFunctions.first().isInfix // TODO?
    override val isSuspend: Boolean
        get() = overriddenFunctions.first().isSuspend // TODO?
    override val isExternal: Boolean
        get() = TODO("Not yet implemented")

    override val member: Member?
        get() = overriddenFunctions.first().member

    override val defaultMember: Member?
        get() = overriddenFunctions.first().defaultMember
}

internal class FakeOverridePropertyDescriptor(
    module: ModuleDescriptor,
    containingClass: ClassDescriptor<*>,
    extensionReceiverParameter: ReceiverParameterDescriptor?,
    valueParameters: List<ValueParameterDescriptor>,
    typeParameters: List<TypeParameterDescriptor>,
    returnType: KotlinType,
    overridden: List<PropertyDescriptor>
) : FakeOverrideCallableMemberDescriptor(
    module, containingClass, extensionReceiverParameter, valueParameters, typeParameters, returnType, overridden
), PropertyDescriptor {
    @Suppress("UNCHECKED_CAST")
    val overriddenProperties: List<PropertyDescriptor>
        get() = super.overridden as List<PropertyDescriptor>

    override val typeParameterTable: TypeParameterTable
        get() = TODO()

    override val isVar: Boolean
        get() = overriddenProperties.any(PropertyDescriptor::isVar)
    override val isLateInit: Boolean
        get() = false
    override val isConst: Boolean
        get() = false

    override val jvmSignature: JvmPropertySignature.KotlinProperty
        get() = TODO("Not yet implemented")

    override val javaField: Field?
        get() = TODO("Not yet implemented")

    override val getter: PropertyGetterDescriptor?
        get() = TODO("Not yet implemented")

    override val setter: PropertySetterDescriptor?
        get() = TODO("Not yet implemented")

    override val caller: Caller<*>
        get() = TODO("Not yet implemented")

    override val member: Member?
        get() = TODO("Not yet implemented")

    override val defaultMember: Member?
        get() = TODO("Not yet implemented")
}
