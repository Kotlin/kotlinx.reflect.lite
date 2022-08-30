/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.builtins

import kotlinx.reflect.lite.name.*

internal enum class JvmPrimitiveType(val primitiveType: PrimitiveType, val javaKeywordName: String, wrapperClassName: String) {
    BOOLEAN(PrimitiveType.BOOLEAN, "boolean", "Boolean"),
    CHAR(PrimitiveType.CHAR, "char", "Character"),
    BYTE(PrimitiveType.BYTE, "byte", "Byte"),
    SHORT(PrimitiveType.SHORT, "short", "Short"),
    INT(PrimitiveType.INT, "int", "Integer"),
    FLOAT(PrimitiveType.FLOAT, "float", "Float"),
    LONG(PrimitiveType.LONG, "long", "Long"),
    DOUBLE(PrimitiveType.DOUBLE, "double", "Double");

    val wrapperFqName = FqName("java.lang.$wrapperClassName")

    companion object {
        private val TYPE_BY_NAME: MutableMap<String, JvmPrimitiveType> =
            HashMap()

        operator fun get(name: String): JvmPrimitiveType =
            TYPE_BY_NAME[name] ?: throw AssertionError("Non-primitive type name passed: $name")

        init {
            for (type in values()) {
                TYPE_BY_NAME[type.javaKeywordName] = type
            }
        }
    }
}
