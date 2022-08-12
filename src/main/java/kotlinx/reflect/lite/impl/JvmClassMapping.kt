@file:JvmName("JvmClassMappingKt")

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*

/**
 * Returns a [KClass] instance corresponding to the given Java [Class] instance.
 */
val <T : Any> Class<T>.kotlin: KClass<T>
    @JvmName("getLiteKClass")
    get() = ReflectionLiteImpl.createKotlinClass(this)

/**
 * Returns a [KPackage] instance corresponding to the given Java [Class] instance.
 */
val <T : Any> Class<T>.kotlinPackage: KPackage<T>
    @JvmName("getLiteKPackage")
    get() = ReflectionLiteImpl.createKotlinPackage(this)


/**
 * Returns a Java [Class] instance corresponding to the given [KClass] instance.
 */
public val <T> KClass<T>.java: Class<T>
    @JvmName("getJavaClass")
    get() = (this as KClassImpl<T>).descriptor.jClass as Class<T>
