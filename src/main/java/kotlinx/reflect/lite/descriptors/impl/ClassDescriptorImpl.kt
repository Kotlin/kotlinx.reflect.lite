package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.metadata.internal.common.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.calls.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.name.*
import java.lang.reflect.*

internal interface AbstractClassDescriptor<T: Any?> : ClassDescriptor<T> {

    override val classId: ClassId
        get() = RuntimeTypeMapper.mapJvmClassToKotlinClassId(jClass)

    override val simpleName: String?
        get() {
            if (jClass.isAnonymousClass) return null

            val classId = classId
            return when {
                classId.isLocal -> calculateLocalClassName(jClass)
                else -> classId.shortClassName
            }
        }

    override val qualifiedName: String?
        get() {
            if (jClass.isAnonymousClass) return null

            val classId = classId
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
}

internal class ClassDescriptorImpl<T : Any?> internal constructor(
    override val jClass: Class<T>
) : AbstractClassDescriptor<T>, ClassBasedDeclarationContainerDescriptorImpl(jClass) {

    private val kmClass: KmClass
        get() {
            val builtinClassId = RuntimeTypeMapper.getKotlinBuiltInClassId(jClass)
            if (builtinClassId != null) {
                val packageName = builtinClassId.packageFqName
                // kotlin.collections -> kotlin/collections/collections.kotlin_builtins
                val resourcePath = packageName.asString().replace('.', '/') + '/' + packageName.shortName() + ".kotlin_builtins"
                val bytes = Unit::class.java.classLoader.getResourceAsStream(resourcePath)?.readBytes()
                    ?: error("No builtins metadata file found: $resourcePath")
                val packageFragment = KotlinCommonMetadata.read(bytes)?.toKmModuleFragment()
                    ?: error("Incompatible metadata version: $resourcePath")
                packageFragment.classes.find { it.name == builtinClassId.asClassName() }?.let { return it }
            }
            val header = jClass.getAnnotation(Metadata::class.java)?.let {
                KotlinClassHeader(it.kind, it.metadataVersion, it.data1, it.data2, it.extraString, it.packageName, it.extraInt)
            } ?: error("@Metadata annotation was not found for ${jClass.name} ")
            return when (val metadata = KotlinClassMetadata.read(header)) {
                is KotlinClassMetadata.Class -> metadata.toKmClass()
                else -> error("Can not create ClassDescriptor for metadata of type $metadata")
            }
        }

    override val module
        get() = ModuleDescriptorImpl(jClass.safeClassLoader)

    override val name: Name
        get() = kmClass.name.substringAfterLast('.').substringAfterLast('/')

    override val constructors: List<ConstructorDescriptor>
        get() = kmClass.constructors.map { ConstructorDescriptorImpl(it, module, this, this) }

    override val nestedClasses: List<ClassDescriptor<*>>
        get() = kmClass.nestedClasses.mapNotNull { module.findClass<Any?>(classId.createNestedClassId(it).asClassName()) }

    override val sealedSubclasses: List<ClassDescriptor<T>>
        get() = kmClass.sealedSubclasses.mapNotNull { module.findClass(it) }

    override val properties: List<PropertyDescriptor>
        get() = kmClass.properties.map { PropertyDescriptorImpl(it, module, this, this) }

    override val functions: List<FunctionDescriptor>
        get() = kmClass.functions.map {
            FunctionDescriptorImpl(it, module, this, this)
        }

    override val memberScope: MemberScope
        get() = MemberScope(
            kmClass.properties.map { PropertyDescriptorImpl(it, module, this, this) }.let { realProperties ->
                realProperties + addPropertyFakeOverrides(this, realProperties)
            },
            kmClass.functions.map { FunctionDescriptorImpl(it, module, this, this) }.let { realFunctions ->
                realFunctions + addFunctionFakeOverrides(this, realFunctions)
            }
        )

    // TODO: static scope
    override val staticScope: MemberScope
        get() = MemberScope(emptyList(), emptyList())

    override val visibility: KVisibility?
        get() = kmClass.flags.toVisibility()

    override val typeParameterTable: TypeParameterTable =
        kmClass.typeParameters.toTypeParameters(this, module, containingClass?.typeParameterTable)

    override val containingClass: ClassDescriptor<*>?
        get() = classId.getOuterClassId()?.let { module.findClass<Any?>(it.asClassName()) }

    override val thisAsReceiverParameter: ReceiverParameterDescriptor =
        ReceiverParameterDescriptorImpl(defaultType)

    override val typeParameters: List<TypeParameterDescriptor>
        get() = typeParameterTable.typeParameters

    override val supertypes: List<KotlinType>
        get() = kmClass.supertypes.map { it.toKotlinType(module, typeParameterTable) }

    override val isInterface: Boolean
        get() = Flag.Class.IS_INTERFACE(kmClass.flags)
    override val isObject: Boolean
        get() = Flag.Class.IS_OBJECT(kmClass.flags)
    override val isCompanion: Boolean
        get() = Flag.Class.IS_COMPANION_OBJECT(kmClass.flags)
    override val isFinal: Boolean
        get() = Flag.Common.IS_FINAL(kmClass.flags)
    override val isOpen: Boolean
        get() = Flag.Common.IS_OPEN(kmClass.flags)
    override val isAbstract: Boolean
        get() = Flag.Common.IS_ABSTRACT(kmClass.flags)
    override val isSealed: Boolean
        get() = Flag.Common.IS_SEALED(kmClass.flags)
    override val isData: Boolean
        get() = Flag.Class.IS_DATA(kmClass.flags)
    override val isInner: Boolean
        get() = Flag.Class.IS_INNER(kmClass.flags)
    override val isFun: Boolean
        get() = Flag.Class.IS_FUN(kmClass.flags)
    override val isValue: Boolean
        get() = Flag.Class.IS_VALUE(kmClass.flags)
}

internal class JavaClassDescriptor<T : Any?> internal constructor(
    override val jClass: Class<T>
): AbstractClassDescriptor<T>, ClassBasedDeclarationContainerDescriptorImpl(jClass) {

    override val module: ModuleDescriptor
        get() = ModuleDescriptorImpl(jClass.safeClassLoader)

    override val name: Name
        get() = jClass.simpleName

    override val constructors: List<ConstructorDescriptor>
        get() = TODO("kdcmd")
    override val nestedClasses: List<ClassDescriptor<*>>
        get() = TODO("Not yet implemented")
    override val sealedSubclasses: List<ClassDescriptor<T>>
        get() = TODO("Not yet implemented")
    override val properties: List<PropertyDescriptor>
        get() = TODO("Not yet implemented")
    override val functions: List<FunctionDescriptor>
        get() = TODO("Not yet implemented")


    override val memberScope: MemberScope
        get() = MemberScope(
            emptyList(), // TODO: Java fields
            jClass.declaredMethods.map { JavaFunctionDescriptor(it, module, this) }.let { realFunctions ->
                realFunctions // todo: add fake overrides
            }
        )

    override val containingClass: ClassDescriptor<*>?
        get() = TODO("Not yet implemented")

    override val thisAsReceiverParameter: ReceiverParameterDescriptor =
        ReceiverParameterDescriptorImpl(defaultType)

    override val typeParameterTable: TypeParameterTable
        get() = TODO("Not yet implemented")
    override val typeParameters: List<TypeParameterDescriptor>
        get() = jClass.typeParameters.map { JavaTypeParameterDescriptor(it, module, this) }
    override val supertypes: List<KotlinType>
        get() = TODO("Not yet implemented")
    override val visibility: KVisibility?
        get() = TODO("Not yet implemented")
    override val isInterface: Boolean
        get() = TODO("Not yet implemented")
    override val isObject: Boolean
        get() = TODO("Not yet implemented")
    override val isCompanion: Boolean
        get() = TODO("Not yet implemented")
    override val isFinal: Boolean
        get() = TODO("Not yet implemented")
    override val isOpen: Boolean
        get() = TODO("Not yet implemented")
    override val isAbstract: Boolean
        get() = TODO("Not yet implemented")
    override val isSealed: Boolean
        get() = TODO("Not yet implemented")
    override val isData: Boolean
        get() = TODO("Not yet implemented")
    override val isInner: Boolean
        get() = TODO("Not yet implemented")
    override val isFun: Boolean
        get() = TODO("Not yet implemented")
    override val isValue: Boolean
        get() = TODO("Not yet implemented")
    override val staticScope: MemberScope
        get() = TODO("Not yet implemented")
}

internal class JavaFunctionDescriptor(
    val method: Method,
    override val module: ModuleDescriptor,
    override val containingClass: JavaClassDescriptor<*>
): FunctionDescriptor {
    override val name: Name
        get() = TODO("Not yet implemented")
    override val container: ClassBasedDeclarationContainerDescriptor
        get() = TODO("Not yet implemented")
    override val dispatchReceiverParameter: ReceiverParameterDescriptor?
        get() = TODO("Not yet implemented")
    override val extensionReceiverParameter: ReceiverParameterDescriptor?
        get() = TODO("Not yet implemented")
    override val valueParameters: List<ValueParameterDescriptor>
        get() = TODO("Not yet implemented")
    override val typeParameters: List<TypeParameterDescriptor>
        get() = TODO("Not yet implemented")
    override val returnType: KotlinType
        get() = method.genericReturnType.javaToKotlinType(module)
    override val visibility: KVisibility?
        get() = TODO("Not yet implemented")
    override val isFinal: Boolean
        get() = TODO("Not yet implemented")
    override val isOpen: Boolean
        get() = TODO("Not yet implemented")
    override val isAbstract: Boolean
        get() = TODO("Not yet implemented")
    override val isReal: Boolean
        get() = TODO("Not yet implemented")
    override val caller: Caller<*>
        get() = TODO("Not yet implemented")
    override val defaultCaller: Caller<*>?
        get() = TODO("Not yet implemented")
    override val isInline: Boolean
        get() = TODO("Not yet implemented")
    override val isExternal: Boolean
        get() = TODO("Not yet implemented")
    override val isOperator: Boolean
        get() = TODO("Not yet implemented")
    override val isInfix: Boolean
        get() = TODO("Not yet implemented")
    override val isSuspend: Boolean
        get() = TODO("Not yet implemented")
    override val isAnnotationConstructor: Boolean
        get() = TODO("Not yet implemented")
    override val signature: JvmMethodSignature?
        get() = TODO("Not yet implemented")
    override val member: Member?
        get() = TODO("Not yet implemented")
    override val defaultMember: Member?
        get() = TODO("Not yet implemented")
}

internal class JavaTypeParameterDescriptor(
    private val typeVariable: TypeVariable<*>,
    private val module: ModuleDescriptor,
    override val containingDeclaration: DeclarationDescriptor,
) : TypeParameterDescriptor {
    override val name: Name
        get() = typeVariable.name

    override val upperBounds: List<KotlinType>
        get() = typeVariable.bounds.map { it.javaToKotlinType(module) }

    override val variance: KVariance
        get() = KVariance.INVARIANT
    override val isReified: Boolean
        get() = false

//    override fun equals(other: Any?): Boolean =
//        other is TypeParameterDescriptor && name == other.name && containingDeclaration == other.containingDeclaration
//
//    override fun hashCode(): Int =
//        name.hashCode() * 31 + containingDeclaration.hashCode()
}

private fun Type.javaToKotlinType(module: ModuleDescriptor): KotlinType {
    return when (this) {
        is Class<*> -> KotlinType(
            module.findClass<Any?>(className) ?: TODO(className),
            emptyList(),
            false
        )
        else -> TODO("Unsupported Java type: $this (${this::class.java})")
    }
}

private val Class<*>.className: ClassName
    get() = name.replace('.', '/').replace('$', '.')
