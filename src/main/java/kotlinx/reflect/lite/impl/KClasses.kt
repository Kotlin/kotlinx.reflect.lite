@file:JvmName("KClasses")

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

/**
 * Returns the primary constructor of this class, or `null` if this class has no primary constructor.
 * See the [Kotlin language documentation](http://kotlinlang.org/docs/reference/classes.html#constructors)
 * for more information.
 */
@SinceKotlin("1.1")
val <T : Any> KClass<T>.primaryConstructor: KFunction<T>?
    get() = constructors.firstOrNull { ((it as KFunctionImpl).descriptor as ConstructorDescriptor).isPrimary }

