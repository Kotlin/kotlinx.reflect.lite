package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.descriptors.ConstructorDescriptor
import kotlinx.reflect.lite.descriptors.FunctionDescriptor
import kotlinx.reflect.lite.descriptors.MemberScope
import kotlinx.reflect.lite.descriptors.ModuleDescriptor
import kotlinx.reflect.lite.descriptors.PropertyDescriptor
import kotlinx.reflect.lite.descriptors.TypeParameterDescriptor
import kotlinx.reflect.lite.impl.KClassImpl
import kotlinx.reflect.lite.name.*

internal class ClassDescriptorImpl internal constructor(
    val kmClass: KmClass,
    override val module: ModuleDescriptor,
    override val classId: ClassId,
    override val kClass: KClassImpl<*>
) : ClassDescriptor {
    override val name: Name
        get() = kmClass.name.substringAfterLast('.').substringAfterLast('/')

    override val constructors: List<ConstructorDescriptor>
        get() = kmClass.constructors.map { ConstructorDescriptorImpl(it, module, this) }

    override val nestedClasses: List<ClassDescriptor>
        get() = kmClass.nestedClasses.map { module.findClass(classId.createNestedClassId(it).asClassName()) }

    override val sealedSubclasses: List<ClassDescriptor>
        get() = kmClass.sealedSubclasses.map(module::findClass)

    override val properties: List<PropertyDescriptor>
        get() = kmClass.properties.map { PropertyDescriptorImpl(it, module, this) }

    override val functions: List<FunctionDescriptor>
        get() = kmClass.functions.map { FunctionDescriptorImpl(it, module, this) }

    // TODO: static members
    // TODO: function and property fake overrides
    override val memberScope: MemberScope
        get() = MemberScope(
            kmClass.properties.map { PropertyDescriptorImpl(it, module, this) },
            kmClass.functions.map { FunctionDescriptorImpl(it, module, this) }
        )

    override val visibility: KVisibility?
        get() = kmClass.flags.toVisibility()

    internal val typeParameterTable: TypeParameterTable =
        kmClass.typeParameters.toTypeParameters(this, module, containingClass?.typeParameterTable)

    internal val containingClass: ClassDescriptorImpl?
        get() = classId.getOuterClassId()?.let { module.findClass(it.asClassName()) as ClassDescriptorImpl? }

    override val typeParameters: List<TypeParameterDescriptor>
        get() = typeParameterTable.typeParameters

    override val supertypes: List<KotlinType>
        get() = kmClass.supertypes.map { it.toKotlinType(module, typeParameterTable) }

    override val isInterface: Boolean
        get() = Flag.Class.IS_INTERFACE(kmClass.flags)
    override val isObject: Boolean
        get() = Flag.Class.IS_OBJECT(kmClass.flags)
    override val isCompanionObject: Boolean
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
    override val isCompanion: Boolean
        get() = Flag.Class.IS_COMPANION_OBJECT(kmClass.flags)
    override val isFun: Boolean
        get() = Flag.Class.IS_FUN(kmClass.flags)
    override val isValue: Boolean
        get() = Flag.Class.IS_VALUE(kmClass.flags)
}