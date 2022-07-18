package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.impl.KotlinType

internal class KTypeImpl(
    val type: KotlinType
): KType {
    override val classifier: KClassifier?
        get() =
            when (val descriptor = type.descriptor) {
                is ClassDescriptor<*> -> {
                    // TODO array types
                    KClassImpl(descriptor)
                }
                is TypeParameterDescriptor -> KTypeParameterImpl(descriptor)
                is TypeAliasDescriptor -> TODO("Type alias classifiers are not yet supported")
                else -> null
            }

    override val arguments: List<KTypeProjection>
        get() {
            val typeArguments = type.arguments
            if (typeArguments.isEmpty()) return emptyList()
            return typeArguments.map { typeProjection ->
                if (typeProjection.isStarProjection) {
                    KTypeProjection.STAR
                } else {
                    val type = KTypeImpl(typeProjection.type)
                    when (typeProjection.projectionKind) {
                        KVariance.INVARIANT -> KTypeProjection.invariant(type)
                        KVariance.IN -> KTypeProjection.contravariant(type)
                        KVariance.OUT -> KTypeProjection.covariant(type)
                    }
                }
            }
        }

    override val isMarkedNullable: Boolean
        get() = type.isMarkedNullable
}
