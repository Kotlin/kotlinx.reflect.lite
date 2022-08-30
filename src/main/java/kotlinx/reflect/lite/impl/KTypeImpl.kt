/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.impl.KotlinType
import java.lang.reflect.*

internal class KTypeImpl(
    val type: KotlinType,
    private val computeJavaType: (() -> Type)? = null
): KType {
    override val classifier: KClassifier?
        get() =
            when (val descriptor = type.descriptor) {
                is ClassDescriptor<*> -> {
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
            return typeArguments.mapIndexed { i, typeProjection ->
                if (typeProjection.isStarProjection) {
                    KTypeProjection.STAR
                } else {
                    val type = KTypeImpl(typeProjection.type,
                        if (computeJavaType == null) null else fun(): Type {
                            return when (val javaType = javaType) {
                                is Class<*> -> {
                                    // It's either an array or a raw type.
                                    if (javaType.isArray) javaType.componentType else Any::class.java
                                }
                                is GenericArrayType -> {
                                    if (i != 0) throw KotlinReflectionInternalError("Array type has been queried for a non-0th argument: $this")
                                    javaType.genericComponentType
                                }
                                is ParameterizedType -> {
                                    val argument = javaType.parameterizedTypeArguments[i]
                                    // In "Foo<out Bar>", the JVM type of the first type argument should be "Bar", not "? extends Bar"
                                    if (argument !is WildcardType) argument
                                    else argument.lowerBounds.firstOrNull() ?: argument.upperBounds.first()
                                }
                                else -> throw KotlinReflectionInternalError("Non-generic type has been queried for arguments: $this")
                            }
                        })
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

    val javaType: Type?
        get() = computeJavaType?.invoke()
}

// Logic from: https://github.com/JetBrains/kotlin/blob/a6b51da3081b11dc2167a0cbb8ebca6f0e805d03/libraries/stdlib/jvm/src/kotlin/reflect/TypesJVM.kt#L37
fun KType.computeJavaType(): Type {
    when (val classifier = classifier) {
        is KClass<*> -> {
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
        }
        else -> throw UnsupportedOperationException("Unsupported type classifier: $this")
    }
    throw UnsupportedOperationException("Unsupported type classifier: ${this.classifier}")
}

// Logic from here: https://github.com/JetBrains/kotlin/blob/fb296b5b95a56fe380bdbe7c9f61d092bd21f275/core/descriptors.runtime/src/org/jetbrains/kotlin/descriptors/runtime/structure/reflectClassUtil.kt#L92
/**
 * @return all arguments of a parameterized type, including those of outer classes in case this type represents an inner generic.
 * The returned list starts with the arguments to the innermost class, then continues with those of its outer class, and so on.
 * For example, for the type `Outer<A, B>.Inner<C, D>` the result would be `[C, D, A, B]`.
 */
internal val Type.parameterizedTypeArguments: List<Type>
    get() {
        if (this !is ParameterizedType) return emptyList()
        if (ownerType == null) return actualTypeArguments.toList()

        return generateSequence(this) { it.ownerType as? ParameterizedType }.flatMap { it.actualTypeArguments.asSequence() }.toList()
    }
