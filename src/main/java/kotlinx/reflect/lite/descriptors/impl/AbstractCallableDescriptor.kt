package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.CallableDescriptor

internal interface AbstractCallableDescriptor : CallableDescriptor {
    val flags: Flags

    override val visibility: KVisibility?
        get() = flags.toVisibility()

    val typeParameterTable: TypeParameterTable

    override val isFinal: Boolean
        get() = Flag.Common.IS_FINAL(flags)
    override val isOpen: Boolean
        get() = Flag.Common.IS_OPEN(flags)
    override val isAbstract: Boolean
        get() = Flag.Common.IS_ABSTRACT(flags)
}