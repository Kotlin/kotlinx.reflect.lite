package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

internal class KParameterImpl(
    val descriptor: ParameterDescriptor,
    override val index: Int,
    override val kind: KParameter.Kind
): KParameter {
    override val name: String
        get() = descriptor.name
}