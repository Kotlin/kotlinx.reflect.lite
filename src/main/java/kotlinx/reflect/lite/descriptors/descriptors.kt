package kotlinx.reflect.lite.descriptors

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.calls.Caller
import kotlinx.reflect.lite.descriptors.impl.KotlinType
import kotlinx.reflect.lite.descriptors.impl.TypeParameterTable
import kotlinx.reflect.lite.impl.KCallableImpl
import kotlinx.reflect.lite.name.*
import java.lang.reflect.*

internal interface ModuleDescriptor {
    fun <T> findClass(name: ClassName): ClassDescriptor<*>
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
    val jClass: Class<*>

    val declaredMembers: Collection<KCallableImpl<*>>
    val allMembers: Collection<KCallableImpl<*>>

    fun findConstructorBySignature(desc: String): Constructor<*>?
    fun findMethodBySignature(name: String, desc: String): Method?
    fun findDefaultConstructor(desc: String): Constructor<*>?
    fun findDefaultMethod(name: String, desc: String, isMember: Boolean): Method?
}

internal interface ClassDescriptor<out T> : DeclarationContainerDescriptor, ClassifierDescriptor {
    val jClass: Class<*>
    val kmClass: KmClass
    val classId: ClassId
    val module: ModuleDescriptor

    val simpleName: String?
    val qualifiedName: String?

    val constructors: List<ConstructorDescriptor>
    val nestedClasses: List<ClassDescriptor<*>>
    val sealedSubclasses: List<ClassDescriptor<T>>
    val properties: List<PropertyDescriptor>
    val functions: List<FunctionDescriptor>
    val memberScope: MemberScope

    val containingClass: ClassDescriptor<*>?

    val typeParameterTable: TypeParameterTable
    val typeParameters: List<TypeParameterDescriptor>
    val supertypes: List<KotlinType>

    val visibility: KVisibility?

    val isInterface: Boolean
    val isObject: Boolean
    val isCompanion: Boolean

    val isFinal: Boolean
    val isOpen: Boolean
    val isAbstract: Boolean
    val isSealed: Boolean
    val isData: Boolean
    val isInner: Boolean
    val isFun: Boolean
    val isValue: Boolean
}

internal class MemberScope(
    val properties: List<PropertyDescriptor>,
    val functions: List<FunctionDescriptor>
)

internal interface CallableDescriptor : DeclarationDescriptor {
    val module: ModuleDescriptor
    val containingClass: ClassDescriptor<*>?
    val container: DeclarationContainerDescriptor

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