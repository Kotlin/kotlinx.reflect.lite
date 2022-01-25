package kotlinx.reflect.lite

/**
 * Represents a declaration of a type parameter of a class or a callable.
 * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/generics.html#generics)
 * for more information.
 */
@SinceKotlin("1.1")
public interface KTypeParameter : KClassifier {
    /**
     * The name of this type parameter as it was declared in the source code.
     */
    public val name: String

    /**
     * Upper bounds, or generic constraints imposed on this type parameter.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/generics.html#upper-bounds)
     * for more information.
     */
    private val upperBounds: List<KType>
        get() = TODO()

    /**
     * Declaration-site variance of this type parameter.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/generics.html#declaration-site-variance)
     * for more information.
     */
    private val variance: KVariance
        get() = TODO()

    /**
     * `true` if this type parameter is `reified`.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/inline-functions.html#reified-type-parameters)
     * for more information.
     */
    public val isReified: Boolean
}