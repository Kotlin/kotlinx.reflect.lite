package kotlinx.reflect.lite

public interface KProperty<out R> : KCallable<R> {
    /**
     * `true` if this property is `lateinit`.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/properties.html#late-initialized-properties)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isLateinit: Boolean

    /**
     * `true` if this property is `const`.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/properties.html#compile-time-constants)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isConst: Boolean
}