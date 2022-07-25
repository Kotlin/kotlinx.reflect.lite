package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.ClassBasedDeclarationContainerDescriptor

internal abstract class KDeclarationContainerImpl : KDeclarationContainer {

    abstract val descriptor: ClassBasedDeclarationContainerDescriptor

    override val members: Collection<KCallable<*>>
        get() = descriptor.allMembers
}
