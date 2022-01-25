package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

internal abstract class KCallableImpl<out R>: KCallable<R> {
    abstract val descriptor: CallableDescriptor

    override val parameters: List<KParameter>
        get() {
            val descriptor = descriptor
            val result = mutableListOf<KParameter>()
            // TODO bound parameter
            for ((index, i) in descriptor.valueParameters.indices.withIndex()) {
                result.add(KParameterImpl(descriptor.valueParameters[i], index, KParameter.Kind.VALUE))
            }
            return result
        }

    override val visibility: KVisibility?
        get() = descriptor.visibility

    override val typeParameters: List<KTypeParameter>
        get() = descriptor.typeParameters.map { KTypeParameterImpl(it) }
}