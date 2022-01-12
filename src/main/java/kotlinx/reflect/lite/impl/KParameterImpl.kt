package kotlinx.reflect.lite.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.*

class KParameterImpl(
    private val kmParam: KmValueParameter
): KParameter {
    override val name: String
        get() = kmParam.name

    override val type: KType?
        get() = kmParam.type?.let(::KTypeImpl)

    override val isVararg: Boolean
        get() = kmParam.varargElementType != null
}