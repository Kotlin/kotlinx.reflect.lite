package kotlinx.reflect.lite.impl

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.*

internal class KClassImpl<T : Any>(
    private val klass: Class<*>,
    private val metadata: KotlinClassMetadata.Class
) : KClass<T> {
    private val kmClass = metadata.toKmClass()
    private val flags: Flags = kmClass.flags

    override val simpleName: String
        get() = kmClass.name

    private val properties: Collection<KProperty<T>>
        get() = kmClass.properties.map(::KPropertyImpl)

    private val functions: Collection<KFunction<T>>
        get() = kmClass.functions.map(::KFunctionImpl)

    override val constructors: Collection<KFunction<T>>
        get() = kmClass.constructors.map(::KConstructorImpl)

    override val isFinal: Boolean
        get() = Flag.Common.IS_FINAL(flags)
    override val isOpen: Boolean
        get() = Flag.Common.IS_OPEN(flags)
    override val isAbstract: Boolean
        get() = Flag.Common.IS_ABSTRACT(flags)
    override val isSealed: Boolean
        get() = Flag.Common.IS_SEALED(flags)
    override val isData: Boolean
        get() = Flag.Class.IS_DATA(flags)
    override val isInner: Boolean
        get() = Flag.Class.IS_INNER(flags)
    override val isCompanion: Boolean
        get() = Flag.Class.IS_COMPANION_OBJECT(flags)
    override val isFun: Boolean
        get() = Flag.Class.IS_FUN(flags)
    override val isValue: Boolean
        get() = Flag.Class.IS_VALUE(flags)
}