package kotlinx.reflect.lite.descriptors

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.impl.KotlinType
import kotlinx.reflect.lite.impl.KClassImpl
import kotlinx.reflect.lite.name.*

internal interface ModuleDescriptor {
    fun findClass(name: ClassName): ClassDescriptor
}

internal interface Annotated {
    private val annotations: Annotations
        get() = TODO()
}

interface Annotations {
    fun getAll(): List<Annotation>

    object Empty : Annotations {
        override fun getAll(): List<Annotation> = emptyList()
    }

    companion object {
        val EMPTY: Empty get() = Empty
    }
}

internal interface DeclarationDescriptor : Annotated {
    val name: Name
}

internal interface DeclarationContainerDescriptor

internal interface ClassDescriptor : DeclarationContainerDescriptor, ClassifierDescriptor {
    val classId: ClassId
    val module: ModuleDescriptor
    val kClass: KClassImpl<*>

    val constructors: List<ConstructorDescriptor>
    val nestedClasses: List<ClassDescriptor>
    val sealedSubclasses: List<ClassDescriptor>
    val properties: List<PropertyDescriptor>
    val functions: List<FunctionDescriptor>
    val memberScope: MemberScope

    val typeParameters: List<TypeParameterDescriptor>
    val supertypes: List<KotlinType>

    val visibility: KVisibility?

    val isInterface: Boolean
    val isObject: Boolean
    val isCompanionObject: Boolean

    val isFinal: Boolean
    val isOpen: Boolean
    val isAbstract: Boolean
    val isSealed: Boolean
    val isData: Boolean
    val isInner: Boolean
    val isCompanion: Boolean
    val isFun: Boolean
    val isValue: Boolean
}

internal class MemberScope(
    val properties: List<PropertyDescriptor>,
    val functions: List<FunctionDescriptor>
)

internal interface CallableDescriptor : DeclarationDescriptor {
    val module: ModuleDescriptor
    val containingClass: ClassDescriptor?

    val valueParameters: List<ValueParameterDescriptor>

    val typeParameters: List<TypeParameterDescriptor>

    val returnType: KotlinType

    val visibility: KVisibility?

    val isFinal: Boolean
    val isOpen: Boolean
    val isAbstract: Boolean
}

internal interface FunctionDescriptor : CallableDescriptor {
    val isInline: Boolean
    val isExternal: Boolean
    val isOperator: Boolean
    val isInfix: Boolean
    val isSuspend: Boolean
}

internal interface ParameterDescriptor : DeclarationDescriptor {
    val containingDeclaration: CallableDescriptor
    val type: KotlinType?
}

internal interface ValueParameterDescriptor : ParameterDescriptor {
    val declaresDefaultValue: Boolean
    private val inheritsDefaultValue: Boolean
        get() = TODO("Is not implemented yet (for KParameter.isOptional 2 case)")

    val varargElementType: KotlinType?
}

internal interface ReceiverParameterDescriptor : ParameterDescriptor

internal interface PropertyDescriptor : CallableDescriptor {
    private val isVar: Boolean
        get() = TODO()
    val isLateInit: Boolean
    val isConst: Boolean
    private val isDelegated: Boolean
        get() = TODO()

    private val getter: PropertyGetterDescriptor?
        get() = TODO()
    private val setter: PropertySetterDescriptor?
        get() = TODO()
}

internal interface PropertyAccessorDescriptor : FunctionDescriptor {
    val property: PropertyDescriptor
}
internal interface PropertyGetterDescriptor : PropertyAccessorDescriptor
internal interface PropertySetterDescriptor : PropertyAccessorDescriptor

internal interface ConstructorDescriptor : FunctionDescriptor

internal interface ClassifierDescriptor : DeclarationDescriptor

internal interface TypeParameterDescriptor : ClassifierDescriptor {
    val containingDeclaration: DeclarationDescriptor
    val upperBounds: List<KotlinType>

    val variance: KVariance
    val isReified: Boolean
}

internal interface TypeAliasDescriptor : ClassifierDescriptor