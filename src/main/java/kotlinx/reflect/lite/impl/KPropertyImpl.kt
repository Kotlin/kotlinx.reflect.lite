package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

internal class KPropertyImpl<T : Any>(
    override val descriptor: PropertyDescriptor
) : KCallableImpl<T>(), KProperty<T> {
    override val name: String
        get() = descriptor.name

    override val isFinal: Boolean
        get() = descriptor.isFinal
    override val isAbstract: Boolean
        get() = descriptor.isAbstract
    override val isOpen: Boolean
        get() = descriptor.isOpen
    override val isSuspend: Boolean
        get() = false

    override val isConst: Boolean
        get() = descriptor.isConst

    override val isLateinit: Boolean
        get() = descriptor.isLateInit
}