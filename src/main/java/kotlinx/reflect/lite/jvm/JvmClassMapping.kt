/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

@file:JvmName("JvmClassMappingKt")

package kotlinx.reflect.lite.jvm

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.impl.KClassImpl
import kotlinx.reflect.lite.impl.ReflectionLiteImpl

/**
 * Returns a [KClass] instance corresponding to the given Java [Class] instance.
 */
public val <T : Any> Class<T>.kotlin: KClass<T>
    @JvmName("getLiteKClass")
    get() = ReflectionLiteImpl.createKotlinClass(this)

/**
 * Returns a [KPackage] instance corresponding to the given Java [Class] instance.
 */
public val <T : Any> Class<T>.kotlinPackage: KPackage<T>
    @JvmName("getLiteKPackage")
    get() = ReflectionLiteImpl.createKotlinPackage(this)

/**
 * Returns a Java [Class] instance corresponding to the given [KClass] instance.
 */
@Suppress("UNCHECKED_CAST")
public val <T> KClass<T>.java: Class<T>
    @JvmName("getJavaClass")
    get() = (this as KClassImpl<T>).descriptor.jClass as Class<T>
