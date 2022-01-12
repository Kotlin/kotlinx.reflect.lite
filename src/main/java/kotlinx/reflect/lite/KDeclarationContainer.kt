package kotlinx.reflect.lite

import kotlin.reflect.*

/**
 * Represents an entity which may contain declarations of any other entities,
 * such as a class or a package.
 */
interface KDeclarationContainer {
    /**
     * All functions and properties accessible in this container.
     */
    //public val members: Collection<KCallable<*>>
}