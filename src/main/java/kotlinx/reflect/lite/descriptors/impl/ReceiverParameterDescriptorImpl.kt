package kotlinx.reflect.lite.descriptors.impl

import kotlinx.reflect.lite.descriptors.*

internal class ReceiverParameterDescriptorImpl(
    override val type: KotlinType
) : ReceiverParameterDescriptor {
    override val name: String
        get() = "<this>"

    // TODO: receiver parameter descriptor does not need this information
    override val containingDeclaration: CallableDescriptor?
        get() = null
}
