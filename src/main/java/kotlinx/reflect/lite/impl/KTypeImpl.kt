package kotlinx.reflect.lite.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.*

class KTypeImpl(
    private val kmType: KmType
): KType {
    override val isMarkedNullable: Boolean
        get() = Flag.Type.IS_NULLABLE(kmType.flags)
}