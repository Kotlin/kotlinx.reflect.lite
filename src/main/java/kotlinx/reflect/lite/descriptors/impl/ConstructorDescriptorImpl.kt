package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.name.*
import java.lang.reflect.*

internal class ConstructorDescriptorImpl(
    private val kmCons: KmConstructor,
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptor<*>,
    override val container: ClassBasedDeclarationContainerDescriptor
) : AbstractFunctionDescriptor(), ConstructorDescriptor {
    override val flags: Flags
        get() = kmCons.flags

    override val name: Name
        get() = "<init>"

    override val signature: JvmMethodSignature?
        get() = kmCons.signature

    override val isPrimary: Boolean
        get() = !Flag.Constructor.IS_SECONDARY(flags)

    override val valueParameters: List<ValueParameterDescriptor>
        get() = kmCons.valueParameters.mapIndexed { index, parameter ->
            ValueParameterDescriptorImpl(parameter, this, index)
        }

    override val typeParameterTable: TypeParameterTable
        get() = emptyList<KmTypeParameter>().toTypeParameters(this, module, containingClass.typeParameterTable)

    override val typeParameters: List<TypeParameterDescriptor>
        get() = emptyList()

    override val dispatchReceiverParameter: ReceiverParameterDescriptor?
        get() = if (containingClass.isInner) containingClass.containingClass!!.thisAsReceiverParameter else null

    override val extensionReceiverParameter: ReceiverParameterDescriptor?
        get() = null

    override val returnType: KotlinType
        get() = containingClass.kotlinType

    private val jvmSignature: JvmFunctionSignature.KotlinConstructor by lazy {
        JvmFunctionSignature.KotlinConstructor(kmCons.signature ?: error("No constructor signature for ${kmCons}"))
    }

    override val member: Member? by lazy {
        container.findConstructorBySignature(jvmSignature.constructorDesc)
    }

    override val defaultMember: Member? by lazy {
        container.findDefaultConstructor(jvmSignature.constructorDesc)
    }

    override fun equals(other: Any?): Boolean {
        val that = (other as? FunctionDescriptor) ?: return false
        return container == that.container && name == that.name && signature == that.signature
    }

    override fun hashCode(): Int =
        (container.hashCode() * 31 + name.hashCode()) * 31 + signature.hashCode()
}
