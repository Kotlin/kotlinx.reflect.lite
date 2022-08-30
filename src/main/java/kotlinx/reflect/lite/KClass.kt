package kotlinx.reflect.lite

import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.misc.*

/**
 * Represents a class and provides introspection capabilities.
 *
 * @param T the type of the class.
 */
public interface KClass<out T> : KDeclarationContainer, KAnnotatedElement, KClassifier {
    /**
     * The simple name of the class as it was declared in the source code,
     * or `null` if the class has no name (if, for example, it is a class of an anonymous object).
     */
    public val simpleName: String?

    /**
     * The fully qualified dot-separated name of the class,
     * or `null` if the class is local or a class of an anonymous object.
     */
    public val qualifiedName: String?

    /**
     * All constructors declared in this class.
     */
    public val constructors: Collection<KFunction<T>>

    /**
     * All classes declared inside this class. This includes both inner and static nested classes.
     */
    public val nestedClasses: Collection<KClass<*>>

    /**
     * The instance of the object declaration, or `null` if this class is not an object declaration.
     */
    private val objectInstance: T?
        get() = notImplemented()

    /**
     * Returns `true` if [value] is an instance of this class on a given platform.
     */
    @SinceKotlin("1.1")
    private fun isInstance(value: Any?): Boolean = notImplemented()

    /**
     * The list of type parameters of this class. This list does *not* include type parameters of outer classes.
     */
    @SinceKotlin("1.1")
    public val typeParameters: List<KTypeParameter>

    /**
     * The list of immediate supertypes of this class, in the order they are listed in the source code.
     */
    @SinceKotlin("1.1")
    public val supertypes: List<KType>

    /**
     * The list of the immediate subclasses if this class is a sealed class, or an empty list otherwise.
     */
    @SinceKotlin("1.3")
    public val sealedSubclasses: List<KClass<T>>

    /**
     * Visibility of this class, or `null` if its visibility cannot be represented in Kotlin.
     */
    @SinceKotlin("1.1")
    public val visibility: KVisibility?

    /**
     * `true` if this class is `final`.
     */
    @SinceKotlin("1.1")
    public val isFinal: Boolean

    /**
     * `true` if this class is `open`.
     */
    @SinceKotlin("1.1")
    public val isOpen: Boolean

    /**
     * `true` if this class is `abstract`.
     */
    @SinceKotlin("1.1")
    public val isAbstract: Boolean

    /**
     * `true` if this class is `sealed`.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/sealed-classes.html)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isSealed: Boolean

    /**
     * `true` if this class is a data class.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/data-classes.html)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isData: Boolean

    /**
     * `true` if this class is an inner class.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/nested-classes.html#inner-classes)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isInner: Boolean

    /**
     * `true` if this class is a companion object.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/object-declarations.html#companion-objects)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isCompanion: Boolean

    /**
     * `true` if this class is a Kotlin functional interface.
     */
    @SinceKotlin("1.4")
    public val isFun: Boolean

    /**
     * `true` if this class is a value class.
     */
    @SinceKotlin("1.5")
    public val isValue: Boolean
}
