package kotlinx.reflect.lite.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.*

internal class KPropertyImpl<T : Any>(
    private val kmProperty: KmProperty
) : KCallableImpl<T>(), KProperty<T> {
    private val flags: Flags = kmProperty.flags

    override val name: String
        get() = kmProperty.name

    override val parameters: List<KParameter>
        get() = emptyList()

    override val isConst: Boolean
        get() = Flag.Property.IS_CONST(flags)
    override val isLateinit: Boolean
        get() = Flag.Property.IS_LATEINIT(flags)

    override val isAbstract: Boolean
        get() = Flag.Common.IS_ABSTRACT(flags)
    override val isFinal: Boolean
        get() = Flag.Common.IS_FINAL(flags)
    override val isOpen: Boolean
        get() = Flag.Common.IS_OPEN(flags)
    override val isSuspend: Boolean
        get() = false
}