package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor

internal class KClassImpl<T : Any?>(
    private val descriptor: ClassDescriptor<T>
) : KClass<T> {

    override val simpleName: String?
        get() = descriptor.simpleName

    override val qualifiedName: String?
        get() = descriptor.qualifiedName

    override val constructors: Collection<KFunction<T>>
        get() =
            if (descriptor.isInterface || descriptor.isObject || descriptor.isCompanionObject) {
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

    override val members: Collection<KCallable<*>>
        get() = descriptor.functions.map(::KFunctionImpl) + descriptor.properties.map(::KPropertyImpl)

    override val visibility: KVisibility?
        get() = descriptor.visibility

    override val typeParameters: List<KTypeParameter>
        get() = descriptor.typeParameters.map { KTypeParameterImpl(it) }

    override val supertypes: List<KType>
        get() = descriptor.supertypes.map(::KTypeImpl)

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
}