/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite

/**
 * Represents an entity which may contain declarations of any other entities,
 * such as a class or a package.
 */
public interface KDeclarationContainer {
    /**
     * Declared functions and properties accessible in this container, including fake overrides.
     * NOTE: does not return inherited and static members for now.
     */
    public val members: Collection<KCallable<*>>
}
