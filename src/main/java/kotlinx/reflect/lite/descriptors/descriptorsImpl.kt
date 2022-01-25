package kotlinx.reflect.lite.descriptors

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.builtins.*
import kotlinx.reflect.lite.impl.KClassImpl
import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.name.*

internal class ModuleDescriptorImpl(private val classLoader: ClassLoader) : ModuleDescriptor {
    override fun findClass(name: ClassName): ClassDescriptor {
        // TODO whyyy java to kotlin class map, and kotlin to java wtf
        val fqName =
            (JavaToKotlinClassMap.mapKotlinToJava(FqName(name.replace('/', '.'))) ?: ClassId(name)).asJavaLookupFqName()
        val jClass = classLoader.tryLoadClass(fqName)
        // todo error message if the class is not found
        return KClassImpl(jClass!!).descriptor
    }
}

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

    override val visibility: KVisibility?
        get() = kmClass.flags.toVisibility()

    override val typeParameters: List<TypeParameterDescriptor>
        get() = TODO("Not yet implemented")

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

internal class ConstructorDescriptorImpl(
    val kmCons: KmConstructor,
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptor
) : AbstractFunctionDescriptor(), ConstructorDescriptor {
    override val flags: Flags
        get() = kmCons.flags

    override val name: Name
        get() = "<init>"

    override val valueParameters: List<ValueParameterDescriptor>
        get() = kmCons.valueParameters.mapIndexed { index, parameter ->
            ValueParameterDescriptorImpl(parameter, this, index)
        }

    override val typeParameters: List<TypeParameterDescriptor>
        get() = emptyList()
}

internal class FunctionDescriptorImpl(
    val kmFunction: KmFunction,
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptorImpl?
) : AbstractFunctionDescriptor() {
    override val flags: Flags
        get() = kmFunction.flags

    override val name: Name
        get() = kmFunction.name

    override val valueParameters: List<ValueParameterDescriptor>
        get() = kmFunction.valueParameters.mapIndexed { index, kmValueParam ->
            ValueParameterDescriptorImpl(kmValueParam, this, index)
        }

    override val typeParameters: List<TypeParameterDescriptor>
        get() = kmFunction.typeParameters.map { TypeParameterDescriptorImpl(it, module, this) }
}

abstract class AbstractFunctionDescriptor : AbstractCallableDescriptor(), FunctionDescriptor {
    override val isInline: Boolean
        get() = Flag.Function.IS_INLINE(flags)
    override val isExternal: Boolean
        get() = Flag.Function.IS_EXTERNAL(flags)
    override val isOperator: Boolean
        get() = Flag.Function.IS_OPERATOR(flags)
    override val isInfix: Boolean
        get() = Flag.Function.IS_INFIX(flags)
    override val isSuspend: Boolean
        get() = Flag.Function.IS_SUSPEND(flags)
}

abstract class AbstractCallableDescriptor : CallableDescriptor {
    protected abstract val flags: Flags

    override val visibility: KVisibility?
        get() = flags.toVisibility()

    override val isFinal: Boolean
        get() = Flag.Common.IS_FINAL(flags)
    override val isOpen: Boolean
        get() = Flag.Common.IS_OPEN(flags)
    override val isAbstract: Boolean
        get() = Flag.Common.IS_ABSTRACT(flags)
}

internal class ValueParameterDescriptorImpl(
    private val kmValueParam: KmValueParameter,
    override val containingDeclaration: AbstractFunctionDescriptor,
    private val index: Int
) : ValueParameterDescriptor {
    override val name: Name
        get() = kmValueParam.name
}

internal class TypeParameterDescriptorImpl(
    private val kmTypeParam: KmTypeParameter,
    private val module: ModuleDescriptor,
    override val containingDeclaration: DeclarationDescriptor
) : TypeParameterDescriptor {
    internal val id: Int get() = kmTypeParam.id

    override val name: Name
        get() = kmTypeParam.name

    override val isReified: Boolean
        get() = Flag.TypeParameter.IS_REIFIED(kmTypeParam.flags)
}

internal class PropertyDescriptorImpl(
    val kmProperty: KmProperty,
    override val module: ModuleDescriptor,
    override val containingClass: ClassDescriptorImpl?
) : AbstractCallableDescriptor(), PropertyDescriptor {
    override val flags: Flags
        get() = kmProperty.flags

    override val name: Name
        get() = kmProperty.name

    override val valueParameters: List<ValueParameterDescriptor>
        get() = emptyList()

    override val typeParameters: List<TypeParameterDescriptor>
        get() = kmProperty.typeParameters.map { TypeParameterDescriptorImpl(it, module, this) }

    override val isConst: Boolean
        get() = Flag.Property.IS_CONST(flags)
    override val isLateInit: Boolean
        get() = Flag.Property.IS_LATEINIT(flags)
}

private fun Flags.toVisibility(): KVisibility? =
    when {
        Flag.Common.IS_PRIVATE(this) -> KVisibility.PRIVATE
        Flag.Common.IS_PRIVATE_TO_THIS(this) -> KVisibility.PRIVATE
        Flag.Common.IS_INTERNAL(this) -> KVisibility.INTERNAL
        Flag.Common.IS_PROTECTED(this) -> KVisibility.PROTECTED
        Flag.Common.IS_PUBLIC(this) -> KVisibility.PUBLIC
        Flag.Common.IS_LOCAL(this) -> null
        else -> error("Declaration with unknown visibility")
    }

