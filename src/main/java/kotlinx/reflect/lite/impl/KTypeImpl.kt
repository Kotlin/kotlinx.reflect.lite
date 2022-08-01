package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.impl.KotlinType
import java.lang.reflect.*

internal class KTypeImpl(
    val type: KotlinType
    // TODO: support computeJavaType:
    // https://github.com/JetBrains/kotlin/blob/fb296b5b95a56fe380bdbe7c9f61d092bd21f275/core/reflection.jvm/src/kotlin/reflect/jvm/internal/KTypeImpl.kt#L40
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

// Logic from: https://github.com/JetBrains/kotlin/blob/a6b51da3081b11dc2167a0cbb8ebca6f0e805d03/libraries/stdlib/jvm/src/kotlin/reflect/TypesJVM.kt#L37
fun KType.computeJavaType(forceWrapper: Boolean = false): Type {
    when (val classifier = classifier) {
        // TODO: support KTypeParameter
        //  is KTypeParameter -> return TypeVariableImpl(classifier)
        is KClass<*> -> {
            // TODO: forceWrapper
            val jClass = (classifier as KClassImpl<*>).descriptor.jClass
            val arguments = arguments
            if (arguments.isEmpty()) return jClass

            if (jClass.isArray) {
                if (jClass.componentType.isPrimitive) return jClass

                val (variance, elementType) = arguments.singleOrNull()
                    ?: throw IllegalArgumentException("kotlin.Array must have exactly one type argument: $this")
                return when (variance) {
                    // Array<in ...> is always erased to Object[], and Array<*> is Object[].
                    null, KVariance.IN -> jClass
                    KVariance.INVARIANT, KVariance.OUT -> {
                        val javaElementType = elementType!!.computeJavaType()
                        if (javaElementType is Class<*>) jClass else TODO("GenericArrayTypeImpl(javaElementType)")
                    }
                }
            }

            // TODO: support inner type
            // return createPossiblyInnerType(jClass, arguments)
        }
        else -> throw UnsupportedOperationException("Unsupported type classifier: $this")
    }
    throw UnsupportedOperationException("Unsupported type classifier: $this")
}
