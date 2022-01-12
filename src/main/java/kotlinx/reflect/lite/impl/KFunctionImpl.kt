package kotlinx.reflect.lite.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.*

internal class KFunctionImpl<T : Any>(
    private val kmFunction: KmFunction
) : KCallableImpl<T>(), KFunction<T> {
    private val flags: Flags = kmFunction.flags

    override val name: String
        get() = kmFunction.name

    override val parameters: List<KParameter>
        get() = kmFunction.valueParameters.map(::KParameterImpl)

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

    override val isAbstract: Boolean
        get() = Flag.Common.IS_ABSTRACT(flags)
    override val isFinal: Boolean
        get() = Flag.Common.IS_FINAL(flags)
    override val isOpen: Boolean
        get() = Flag.Common.IS_OPEN(flags)
}