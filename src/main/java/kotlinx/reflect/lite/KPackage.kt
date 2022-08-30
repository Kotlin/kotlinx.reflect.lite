/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite

/**
 * Represents a Kotlin class file that is a compiled Kotlin file facade or multi-file class part.
 *
 * @param T the type of the class.
 */
public interface KPackage<out T> : KDeclarationContainer
