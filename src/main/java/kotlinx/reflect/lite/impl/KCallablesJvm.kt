// Copied from: https://github.com/JetBrains/kotlin/blob/a7c80f2df1fb3efdb139914a36d37692574f0034/core/reflection.jvm/src/kotlin/reflect/jvm/KCallablesJvm.kt
@file:JvmName("KCallablesJvm")
package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import java.lang.reflect.AccessibleObject

/**
 * Provides a way to suppress JVM access checks for a callable.
 *
 * @getter returns `true` if JVM access checks are suppressed for this callable object.
 *         For a property, that means that all its accessors (getter, and setter for `var` properties) are accessible.
 *
 * @setter if set to `true`, suppresses JVM access checks for this callable object.
 *         For a property, both accessors are made accessible.
 *
 * @see [java.lang.reflect.AccessibleObject]
 */
var KCallable<*>.isAccessible: Boolean
    get() {
        return when (this) {
            is KMutableProperty ->
                javaField?.isAccessible ?: true &&
                        javaGetter?.isAccessible ?: true &&
                        javaSetter?.isAccessible ?: true
            is KProperty ->
                javaField?.isAccessible ?: true &&
                        javaGetter?.isAccessible ?: true
            is KProperty.Getter ->
                property.javaField?.isAccessible ?: true &&
                        javaMethod?.isAccessible ?: true
            is KMutableProperty.Setter<*> ->
                property.javaField?.isAccessible ?: true &&
                        javaMethod?.isAccessible ?: true
            is KFunction ->
                javaMethod?.isAccessible ?: true &&
                        ((this as? KCallableImpl<*>)?.descriptor?.defaultCaller?.member as? AccessibleObject)?.isAccessible ?: true &&
                        this.javaConstructor?.isAccessible ?: true
            else -> throw UnsupportedOperationException("Unknown callable: $this ($javaClass)")
        }
    }
    set(value) {
        when (this) {
            is KMutableProperty -> {
                javaField?.isAccessible = value
                javaGetter?.isAccessible = value
                javaSetter?.isAccessible = value
            }
            is KProperty -> {
                javaField?.isAccessible = value
                javaGetter?.isAccessible = value
            }
            is KProperty.Getter -> {
                property.javaField?.isAccessible = value
                javaMethod?.isAccessible = value
            }
            is KMutableProperty.Setter<*> -> {
                property.javaField?.isAccessible = value
                javaMethod?.isAccessible = value
            }
            is KFunction -> {
                javaMethod?.isAccessible = value
                ((this as? KCallableImpl<*>)?.descriptor?.defaultCaller?.member as? AccessibleObject)?.isAccessible = true
                this.javaConstructor?.isAccessible = value
            }
            else -> throw UnsupportedOperationException("Unknown callable: $this ($javaClass)")
        }
    }