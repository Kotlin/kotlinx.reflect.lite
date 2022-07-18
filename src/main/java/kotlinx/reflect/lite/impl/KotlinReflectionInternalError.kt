// Copy of: https://github.com/JetBrains/kotlin/blob/master/core/reflection.jvm/src/kotlin/reflect/jvm/internal/KotlinReflectionInternalError.kt
package kotlinx.reflect.lite.impl

/**
 * Signals that Kotlin reflection had reached an inconsistent state from which it cannot recover.
 * @suppress
 */
class KotlinReflectionInternalError(message: String) : Error(message)
