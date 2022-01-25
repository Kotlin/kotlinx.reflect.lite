package kotlinx.reflect.lite.impl

import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.misc.*

internal class KClassImpl<T : Any>(
    val jClass: Class<T>
) : KClass<T> {
    val descriptor: ClassDescriptor = createClassDescriptor()

    private fun createClassDescriptor(): ClassDescriptor {
        val header = with(jClass.getAnnotation(Metadata::class.java)) {
            KotlinClassHeader(kind, metadataVersion, data1, data2, extraString, packageName, extraInt)
        }
        val metadata = KotlinClassMetadata.read(header)
        val kmClass = (metadata as? KotlinClassMetadata.Class)?.toKmClass()
            ?: error("KotlinClassMetadata.Class metadata is only supported for now")
        val module = ModuleDescriptorImpl(jClass.safeClassLoader)
        return ClassDescriptorImpl(kmClass, module, jClass.classId, this)
    }

    override val simpleName: String?
        get() {
            if (jClass.isAnonymousClass) return null

            val classId = jClass.classId
            return when {
                classId.isLocal -> calculateLocalClassName(jClass)
                else -> classId.shortClassName
            }
        }

    override val qualifiedName: String?
        get() {
            if (jClass.isAnonymousClass) return null

            val classId = jClass.classId
            return when {
                classId.isLocal -> null
                else -> classId.asSingleFqName().asString()
            }
        }

    private fun calculateLocalClassName(jClass: Class<*>): String {
        val name = jClass.simpleName
        jClass.enclosingMethod?.let { method ->
            return name.substringAfter(method.name + "$")
        }
        jClass.enclosingConstructor?.let { constructor ->
            return name.substringAfter(constructor.name + "$")
        }
        return name.substringAfter('$')
    }

    override val constructors: Collection<KFunction<T>>
        get() = descriptor.constructors.map {
            @Suppress("UNCHECKED_CAST")
            KFunctionImpl(it) as KFunction<T>
        }

    override val nestedClasses: Collection<KClass<*>>
        get() = descriptor.nestedClasses.map { nestedClassDesc ->
            nestedClassDesc.kClass.jClass.let { KClassImpl(it) }
        }

    override val sealedSubclasses: List<KClass<T>>
        get() = descriptor.sealedSubclasses.mapNotNull { subclassDesc ->
            @Suppress("UNCHECKED_CAST")
            val jClass = subclassDesc.kClass.jClass as Class<out T>?
            jClass?.let { KClassImpl(it) }
        }

    override val visibility: KVisibility?
        get() = descriptor.visibility

    override val typeParameters: List<KTypeParameter>
        get() = descriptor.typeParameters.map { KTypeParameterImpl(it) }

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