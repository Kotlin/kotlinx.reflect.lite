/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite

/**
 * Represents a function with introspection capabilities.
 */
public interface KFunction<out R> : KCallable<R>, Function<R> {
    /**
     * `true` if this function is `inline`.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/inline-functions.html)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isInline: Boolean

    /**
     * `true` if this function is `external`.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/java-interop.html#using-jni-with-kotlin)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isExternal: Boolean

    /**
     * `true` if this function is `operator`.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/operator-overloading.html)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isOperator: Boolean

    /**
     * `true` if this function is `infix`.
     * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/functions.html#infix-notation)
     * for more information.
     */
    @SinceKotlin("1.1")
    public val isInfix: Boolean

    /**
     * `true` if this is a suspending function.
     */
    @SinceKotlin("1.1")
    public override val isSuspend: Boolean
}
