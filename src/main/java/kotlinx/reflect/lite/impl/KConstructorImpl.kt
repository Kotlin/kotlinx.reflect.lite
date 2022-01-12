package kotlinx.reflect.lite.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.*

internal class KConstructorImpl<T : Any>(
    private val kmCons: KmConstructor
) : KCallableImpl<T>(), KFunction<T> {
    private val flags: Flags = kmCons.flags

    override val name: String
        get() = "<init>"

    override val parameters: List<KParameter>
        get() = kmCons.valueParameters.map(::KParameterImpl)

    override val isInline: Boolean
        get() = false
    override val isExternal: Boolean
        get() = false
    override val isOperator: Boolean
        get() = false
    override val isInfix: Boolean
        get() = false
    override val isAbstract: Boolean
        get() = Flag.IS_ABSTRACT(flags)
    override val isFinal: Boolean
        get() = Flag.IS_FINAL(flags)
    override val isOpen: Boolean
        get() = Flag.IS_OPEN(flags)
    override val isSuspend: Boolean
        get() = false
}