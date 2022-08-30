package kotlinx.reflect.lite

/**
 * Represents variance applied to a type parameter on the declaration site (*declaration-site variance*),
 * or to a type in a projection (*use-site variance*).
 *
 * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/generics.html#variance)
 * for more information.
 *
 * @see [KTypeParameter.variance]
 * @see [KTypeProjection]
 */
@SinceKotlin("1.1")
public enum class KVariance {
    /**
     * The affected type parameter or type is *invariant*, which means it has no variance applied to it.
     */
    INVARIANT,

    /**
     * The affected type parameter or type is *contravariant*. Denoted by the `in` modifier in the source code.
     */
    IN,

    /**
     * The affected type parameter or type is *covariant*. Denoted by the `out` modifier in the source code.
     */
    OUT,
}
