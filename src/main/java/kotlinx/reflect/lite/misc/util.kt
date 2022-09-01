/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

// Most logic copied from: https://github.com/JetBrains/kotlin/blob/master/core/descriptors.runtime/src/org/jetbrains/kotlin/descriptors/runtime/structure/reflectClassUtil.kt
package kotlinx.reflect.lite.misc

import kotlinx.reflect.lite.name.*
import java.lang.reflect.Array

internal val Class<*>.safeClassLoader: ClassLoader
    get() = classLoader ?: ClassLoader.getSystemClassLoader()

private val PRIMITIVE_CLASSES =
    listOf(Boolean::class, Byte::class, Char::class, Double::class, Float::class, Int::class, Long::class, Short::class)
private val PRIMITIVE_TO_WRAPPER = PRIMITIVE_CLASSES.map { it.javaPrimitiveType to it.javaObjectType }.toMap()

internal val Class<*>.wrapperByPrimitive: Class<*>?
    get() = PRIMITIVE_TO_WRAPPER[this]

/**
 * NOTE: does not perform a Java -> Kotlin mapping. If this is not expected, consider using KClassImpl#classId instead
 */
internal val Class<*>.classId: ClassId
    get() = when {
        isPrimitive -> throw IllegalArgumentException("Can't compute ClassId for primitive type: $this")
        isArray -> throw IllegalArgumentException("Can't compute ClassId for array type: $this")
        enclosingMethod != null || enclosingConstructor != null || simpleName.isEmpty() -> {
            val fqName = FqName(name)
            ClassId(fqName.parent(), FqName(fqName.shortName()), /* local = */ true)
        }
        else -> declaringClass?.classId?.createNestedClassId(simpleName) ?: ClassId.topLevel(FqName(name))
    }

internal fun Class<*>.createArrayType(): Class<*> =
    Array.newInstance(this, 0)::class.java

internal fun ClassLoader.tryLoadClass(fqName: String) =
    try {
        Class.forName(fqName, false, this)
    } catch (e: ClassNotFoundException) {
        null
    }

internal fun notImplemented(): Nothing = TODO("This functionality is not yet supported in kotlinx.reflect.lite")
