/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

// Copy of: https://github.com/JetBrains/kotlin/blob/master/core/reflection.jvm/src/kotlin/reflect/jvm/internal/KotlinReflectionInternalError.kt
package kotlinx.reflect.lite.impl

/**
 * Signals that Kotlin reflection had reached an inconsistent state from which it cannot recover.
 * @suppress
 */
public class KotlinReflectionInternalError(message: String) : Error(message)
