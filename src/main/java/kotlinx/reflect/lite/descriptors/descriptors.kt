package kotlinx.reflect.lite.descriptors

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.KClassImpl
import kotlinx.reflect.lite.name.*
import kotlinx.reflect.lite.types.KotlinType

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

internal interface DeclarationContainerDescriptor {
    private val members: Collection<CallableDescriptor>
        get() = TODO()
}

internal interface ClassDescriptor : DeclarationContainerDescriptor, ClassifierDescriptor {
    val classId: ClassId
    val module: ModuleDescriptor
    val kClass: KClassImpl<*>

    val constructors: List<ConstructorDescriptor>
    val nestedClasses: List<ClassDescriptor>
    val sealedSubclasses: List<ClassDescriptor>

    public val typeParameters: List<TypeParameterDescriptor>

    private val supertypes: List<KotlinType>
        get() = TODO()

    public val visibility: KVisibility?

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

internal interface CallableDescriptor : DeclarationDescriptor {
    val module: ModuleDescriptor
    val containingClass: ClassDescriptor?

    val valueParameters: List<ValueParameterDescriptor>

    public val typeParameters: List<TypeParameterDescriptor>

    private val returnType: KType
        get() = TODO()

    public val visibility: KVisibility?

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
}

internal interface ValueParameterDescriptor : ParameterDescriptor

internal interface ReceiverParameterDescriptor : ParameterDescriptor

internal interface PropertyDescriptor : CallableDescriptor {
    private val isVar: Boolean
        get() = TODO()
    public val isLateInit: Boolean
    public val isConst: Boolean
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
    private val upperBounds: List<KotlinType>
        get() = TODO()
    private val variance: KVariance
        get() = TODO()
    val isReified: Boolean
}

