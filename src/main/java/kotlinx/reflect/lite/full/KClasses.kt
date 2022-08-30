/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

@file:JvmName("KClasses")

package kotlinx.reflect.lite.full

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.ConstructorDescriptor
import kotlinx.reflect.lite.impl.KFunctionImpl

/**
 * Returns the primary constructor of this class, or `null` if this class has no primary constructor.
 * See the [Kotlin language documentation](http://kotlinlang.org/docs/reference/classes.html#constructors)
 * for more information.
 */
@SinceKotlin("1.1")
public val <T : Any> KClass<T>.primaryConstructor: KFunction<T>?
    get() = constructors.firstOrNull { ((it as KFunctionImpl).descriptor as ConstructorDescriptor).isPrimary }
