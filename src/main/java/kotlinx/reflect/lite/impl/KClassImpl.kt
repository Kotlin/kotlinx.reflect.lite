package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.internal.*

internal class KClassImpl<T : Any?>(
    override val descriptor: ClassDescriptor<T>
) : KClass<T>, KDeclarationContainerImpl() {

    override val simpleName: String?
        get() = descriptor.simpleName

    override val qualifiedName: String?
        get() = descriptor.qualifiedName

    override val constructors: Collection<KFunction<T>>
        get() =
            if (descriptor.isInterface || descriptor.isObject || descriptor.isCompanion) {
                emptyList()
            } else {
                descriptor.constructors.map {
                    @Suppress("UNCHECKED_CAST")
                    KFunctionImpl(it) as KFunction<T>
                }
            }

    override val nestedClasses: Collection<KClass<*>>
        get() = descriptor.nestedClasses.map(::KClassImpl)

    override val sealedSubclasses: List<KClass<T>>
        get() = descriptor.sealedSubclasses.map(::KClassImpl)

    override val visibility: KVisibility?
        get() = descriptor.visibility

    override val typeParameters: List<KTypeParameter>
        get() = descriptor.typeParameters.map { KTypeParameterImpl(it) }

    // Logic from: https://github.com/JetBrains/kotlin/blob/bfa3f89aeb727518703f0a167153cb048724a6d1/core/reflection.jvm/src/kotlin/reflect/jvm/internal/KClassImpl.kt#L124
    override val supertypes: List<KType> by ReflectProperties.lazySoft {
        val kotlinTypes = descriptor.supertypes
        val result = ArrayList<KTypeImpl>(kotlinTypes.size)
        kotlinTypes.mapTo(result) { kotlinType ->
            KTypeImpl(kotlinType) {
                val superClass = kotlinType.descriptor
                if (superClass !is ClassDescriptor<*>) throw KotlinReflectionInternalError("Supertype not a class: $superClass")
                val superJavaClass = superClass.jClass
                if (descriptor.jClass.superclass == superJavaClass) {
                    descriptor.jClass.genericSuperclass
                } else {
                    val index = descriptor.jClass.interfaces.indexOf(superJavaClass)
                    if (index < 0) throw KotlinReflectionInternalError("No superclass of $this in Java reflection for $superClass")
                    descriptor.jClass.genericInterfaces[index]
                }
            }
        }
    }

    override val isFinal: Boolean
        get() = descriptor.isFinal
    override val isOpen: Boolean
        get() = descriptor.isOpen
    override val isAbstract: Boolean
        get() = descriptor.isAbstract
    override val isSealed: Boolean
        get() = descriptor.isSealed
    override val isData: Boolean
        get() = descriptor.isData
    override val isInner: Boolean
        get() = descriptor.isInner
    override val isCompanion: Boolean
        get() = descriptor.isCompanion
    override val isFun: Boolean
        get() = descriptor.isFun
    override val isValue: Boolean
        get() = descriptor.isValue

    override fun equals(other: Any?): Boolean =
        other is KClassImpl<*> && descriptor == other.descriptor

    override fun hashCode(): Int = descriptor.hashCode()
}
