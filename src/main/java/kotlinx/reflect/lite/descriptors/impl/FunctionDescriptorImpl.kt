package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.calls.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.internal.*
import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.name.*
import java.lang.reflect.*

internal abstract class AbstractFunctionDescriptor : AbstractCallableDescriptor, FunctionDescriptor {
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
    override val isReal: Boolean
        get() = true

    override val isAnnotationConstructor: Boolean
        get() = name == "<init>" && container.jClass.isAnnotation

    override val caller: Caller<*> by ReflectProperties.lazy {
        createCaller(member)
    }

    override val defaultCaller: Caller<*>? by ReflectProperties.lazy {
        defaultMember?.let { createCaller(it) }
    }

    private fun createCaller(member: Member?) =
        when (member) {
            is Constructor<*> ->
                createConstructorCaller(member)
            is Method -> when {
                !Modifier.isStatic(member.modifiers) ->
                    createInstanceMethodCaller(member)
                containingClass?.isCompanion == false ->
                    createJvmStaticInObjectCaller(member)
                else ->
                    createStaticMethodCaller(member)
            }
            else -> throw KotlinReflectionInternalError("Could not compute caller for function: $this (member = $member)")
        }

    // TODO: support bound receiver -> CallerImpl.BoundConstructor
    private fun createConstructorCaller(member: Constructor<*>): CallerImpl<Constructor<*>> {
        return CallerImpl.Constructor(member)
    }

    // TODO: support bound receiver -> CallerImpl.Method.BoundInstance
    private fun createInstanceMethodCaller(member: Method): CallerImpl<Method> {
        return CallerImpl.Method.Instance(member)
    }

    // TODO: support bound receiver -> CallerImpl.Method.BoundInstance
    private fun createStaticMethodCaller(member: Method): CallerImpl<Method> {
        return CallerImpl.Method.Static(member)
    }

    // TODO: support bound receiver -> CallerImpl.Method.BoundInstance
    private fun createJvmStaticInObjectCaller(member: Method): CallerImpl<Method> {
        return CallerImpl.Method.JvmStaticInObject(member)
    }
}

internal class FunctionDescriptorImpl(
    val kmFunction: KmFunction,
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptor<*>?,
    override val container: ClassBasedDeclarationContainerDescriptor
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

    override val dispatchReceiverParameter: ReceiverParameterDescriptor?
        get() = containingClass?.let { ReceiverParameterDescriptorImpl(it.defaultType, this) }

    override val extensionReceiverParameter: ReceiverParameterDescriptor?
        get() = kmFunction.receiverParameterType?.let {
            ReceiverParameterDescriptorImpl(it.toKotlinType(module, typeParameterTable), this)
        }

    override val returnType: KotlinType
        get() = kmFunction.returnType.toKotlinType(module, typeParameterTable)

    private val jvmSignature: JvmFunctionSignature.KotlinFunction by ReflectProperties.lazy {
        JvmFunctionSignature.KotlinFunction(kmFunction.signature ?: error("No signature for ${kmFunction}"))
    }

    override val member: Member? by ReflectProperties.lazy {
        container.findMethodBySignature(jvmSignature.methodName, jvmSignature.methodDesc)
    }

    override val defaultMember: Member? by ReflectProperties.lazy {
        container.findDefaultMethod(jvmSignature.methodName, jvmSignature.methodDesc, !Modifier.isStatic(caller.member!!.modifiers))
    }
}
