package kotlinx.reflect.lite

/**
 * Represents an entity which may contain declarations of any other entities,
 * such as a class or a package.
 */
interface KDeclarationContainer {
    /**
     * All functions and properties accessible in this container.
     */
    private val members: Collection<KCallable<*>>
        get() = TODO()
}