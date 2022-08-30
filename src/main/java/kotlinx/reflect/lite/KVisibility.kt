/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite

/**
 * Visibility is an aspect of a Kotlin declaration regulating where that declaration is accessible in the source code.
 * Visibility can be changed with one of the following modifiers: `public`, `protected`, `internal`, `private`.
 *
 * Note that some Java visibilities such as package-private and protected (which also gives access to items from the same package)
 * cannot be represented in Kotlin, so there's no [KVisibility] value corresponding to them.
 *
 * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/visibility-modifiers.html)
 * for more information.
 */
@SinceKotlin("1.1")
public enum class KVisibility {
    /**
     * Visibility of declarations marked with the `public` modifier, or with no modifier at all.
     */
    PUBLIC,

    /**
     * Visibility of declarations marked with the `protected` modifier.
     */
    PROTECTED,

    /**
     * Visibility of declarations marked with the `internal` modifier.
     */
    INTERNAL,

    /**
     * Visibility of declarations marked with the `private` modifier.
     */
    PRIVATE,
}
