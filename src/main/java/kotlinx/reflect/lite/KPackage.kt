package kotlinx.reflect.lite

/**
 * Represents a Kotlin class file that is a compiled Kotlin file facade or multi-file class part.
 *
 * @param T the type of the class.
 */
public interface KPackage<out T> : KDeclarationContainer
