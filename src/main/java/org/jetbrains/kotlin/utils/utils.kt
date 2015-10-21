package org.jetbrains.kotlin.utils

fun <T : Any> T?.singletonOrEmptyList(): List<T> =
        if (this != null) listOf(this) else emptyList()

inline fun <T, C: Collection<T>> C.ifEmpty(body: () -> C): C =
        if (isEmpty()) body() else this
