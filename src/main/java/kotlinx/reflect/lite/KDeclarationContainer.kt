package kotlinx.reflect.lite

/**
 * Represents an entity which may contain declarations of any other entities,
 * such as a class or a package.
 */
interface KDeclarationContainer {
    /**
     * All functions and properties accessible in this container.
     * TODO: inherited, static and extension members are not supported yet
     * NOTE: kotlinx-metadata-jvm can not provide inherited and static members - it's a low level api
     */
    public val members: Collection<KCallable<*>>
}