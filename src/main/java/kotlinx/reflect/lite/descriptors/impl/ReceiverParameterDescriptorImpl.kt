package kotlinx.reflect.lite.descriptors.impl

import kotlinx.reflect.lite.descriptors.*

internal class ReceiverParameterDescriptorImpl(
    override val type: KotlinType,
    override val containingDeclaration: CallableDescriptor
) : ReceiverParameterDescriptor {
    override val name: String
        get() = "<this>"
}
