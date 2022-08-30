/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.metadata.internal.common.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.*
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

internal class ClassDescriptorImpl<T : Any?>(
    override val jClass: Class<T>
) : AbstractClassDescriptor<T>, ClassBasedDeclarationContainerDescriptorImpl(jClass) {

    private val kmClass: KmClass by lazy {
        val builtinClassId = RuntimeTypeMapper.getKotlinBuiltInClassId(jClass)
        if (builtinClassId != null) {
            val packageName = builtinClassId.packageFqName
            // kotlin.collections -> kotlin/collections/collections.kotlin_builtins
            val resourcePath = packageName.asString().replace('.', '/') + '/' + packageName.shortName() + ".kotlin_builtins"
            val bytes = Unit::class.java.classLoader.getResourceAsStream(resourcePath)?.readBytes()
                ?: error("No builtins metadata file found: $resourcePath")
            val packageFragment = KotlinCommonMetadata.read(bytes)?.toKmModuleFragment()
                ?: error("Incompatible metadata version: $resourcePath")
            packageFragment.classes.find { it.name == builtinClassId.asClassName() }?.let { return@lazy it }
        }
        val header = jClass.getAnnotation(Metadata::class.java)?.let {
            KotlinClassHeader(it.kind, it.metadataVersion, it.data1, it.data2, it.extraString, it.packageName, it.extraInt)
        } ?: error("@Metadata annotation was not found for ${jClass.name} ")
        return@lazy when (val metadata = KotlinClassMetadata.read(header)) {
            is KotlinClassMetadata.Class -> metadata.toKmClass()
            else -> error("Can not create ClassDescriptor for metadata of type $metadata")
        }
    }

    override val name: Name by lazy {
        kmClass.name.substringAfterLast('.').substringAfterLast('/')
    }

    override val constructors: List<ConstructorDescriptor> by lazy {
        kmClass.constructors.map { ConstructorDescriptorImpl(it, module, this, this) }
    }

    override val nestedClasses: List<ClassDescriptor<*>> by lazy {
        kmClass.nestedClasses.mapNotNull { module.findClass<Any?>(classId.createNestedClassId(it).asClassName()) }
    }

    override val sealedSubclasses: List<ClassDescriptor<T>> by lazy {
        kmClass.sealedSubclasses.mapNotNull { module.findClass(it) }
    }

    override val memberScope: MemberScope by lazy {
        MemberScope(
            kmClass.properties.map { PropertyDescriptorImpl(it, module, this, this) }.let { realProperties ->
                realProperties + addPropertyFakeOverrides(this, realProperties)
            },
            kmClass.functions.map { FunctionDescriptorImpl(it, module, this, this) }.let { realFunctions ->
                realFunctions + addFunctionFakeOverrides(this, realFunctions)
            }
        )
    }

    // TODO: static scope
    override val staticScope: MemberScope by lazy {
        MemberScope(emptyList(), emptyList())
    }

    override val visibility: KVisibility? by lazy {
        kmClass.flags.toVisibility()
    }

    override val typeParameterTable: TypeParameterTable by lazy {
        kmClass.typeParameters.toTypeParameters(this, module, containingClass?.typeParameterTable)
    }

    override val typeParameters: List<TypeParameterDescriptor> by lazy {
        typeParameterTable.typeParameters
    }

    override val supertypes: List<KotlinType> by lazy {
        kmClass.supertypes.map { it.toKotlinType(module, typeParameterTable) }
    }

    override val containingClass: ClassDescriptor<*>? by lazy {
        classId.getOuterClassId()?.let { module.findClass<Any?>(it.asClassName()) }
    }

    override val thisAsReceiverParameter: ReceiverParameterDescriptor by lazy {
        ReceiverParameterDescriptorImpl(defaultType)
    }

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

internal class JavaClassDescriptor<T : Any?>(
    override val jClass: Class<T>
): AbstractClassDescriptor<T>, ClassBasedDeclarationContainerDescriptorImpl(jClass) {

    override val name: Name by lazy {
        jClass.simpleName
    }

    override val constructors: List<ConstructorDescriptor> by lazy {
        jClass.constructors.map { JavaConstructorDescriptorImpl(it, module, this, this) }
    }

    override val nestedClasses: List<ClassDescriptor<*>> by lazy {
        jClass.declaredClasses.mapNotNull { module.findClass<Any?>(classId.createNestedClassId(it.simpleName).asClassName()) }
    }

    override val sealedSubclasses: List<ClassDescriptor<T>>
        get() = emptyList()

    override val memberScope: MemberScope by lazy {
        MemberScope(
            emptyList(), // TODO: Java fields
            jClass.declaredMethods.map { JavaFunctionDescriptorImpl(it, module, this) }.let { realFunctions ->
                realFunctions // todo: add fake overrides
            }
        )
    }

    override val staticScope: MemberScope
        get() = TODO("Not yet implemented")

    override val containingClass: ClassDescriptor<*>? by lazy {
        classId.getOuterClassId()?.let { module.findClass<Any?>(it.asClassName()) as JavaClassDescriptor? }
    }

    override val thisAsReceiverParameter: ReceiverParameterDescriptor by lazy {
        ReceiverParameterDescriptorImpl(defaultType)
    }

    override val typeParameterTable: TypeParameterTable by lazy {
        emptyList<KmTypeParameter>().toTypeParameters(this, module, containingClass?.typeParameterTable)
    }

    override val typeParameters: List<TypeParameterDescriptor> by lazy {
        jClass.typeParameters.map { JavaTypeParameterDescriptorImpl(it, module, this) }
    }

    override val supertypes: List<KotlinType> by lazy {
        (listOfNotNull(jClass.genericSuperclass) + jClass.genericInterfaces).map { it.javaToKotlinType(module) }
    }

    override val visibility: KVisibility?
        get() = TODO("Not yet implemented")

    override val isInterface: Boolean
        get() = jClass.isInterface && !jClass.isAnnotation

    override val isObject: Boolean
        get() = false

    override val isCompanion: Boolean
        get() = false

    override val isFinal: Boolean
        get() = Modifier.isFinal(jClass.modifiers)

    override val isOpen: Boolean
        get() = !isFinal && !isAbstract

    override val isAbstract: Boolean
        get() = Modifier.isAbstract(jClass.modifiers)

    override val isSealed: Boolean
        get() = false

    override val isData: Boolean
        get() = false

    override val isInner: Boolean by lazy {
        jClass.declaringClass != null && !Modifier.isStatic(jClass.modifiers)
    }

    override val isFun: Boolean
        get() = false

    override val isValue: Boolean
        get() = false
}
