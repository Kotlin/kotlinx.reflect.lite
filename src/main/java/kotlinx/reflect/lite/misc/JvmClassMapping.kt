// Most logic copied from: https://github.com/JetBrains/kotlin/blob/master/libraries/stdlib/jvm/runtime/kotlin/jvm/JvmClassMapping.kt
package kotlinx.reflect.lite.misc

import java.lang.Boolean as JavaLangBoolean
import java.lang.Byte as JavaLangByte
import java.lang.Character as JavaLangCharacter
import java.lang.Double as JavaLangDouble
import java.lang.Float as JavaLangFloat
import java.lang.Integer as JavaLangInteger
import java.lang.Long as JavaLangLong
import java.lang.Short as JavaLangShort
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.impl.KClassImpl

/**
 * Returns a Java [Class] instance corresponding to the given [KClass] instance.
 */
public val <T> KClass<T>.java: Class<T>
    get() = (this as KClassImpl<T>).descriptor.jClass as Class<T> // TODO: revealing the descriptor

/**
 * Returns a Java [Class] instance representing the primitive type corresponding to the given [KClass] if it exists.
 */
public val <T : Any> KClass<T>.javaPrimitiveType: Class<T>?
    get() {
        val thisJClass = this.java
        if (thisJClass.isPrimitive) return thisJClass as Class<T>

        return when (thisJClass.name) {
            "java.lang.Boolean"   -> Boolean::class.java
            "java.lang.Character" -> Char::class.java
            "java.lang.Byte"      -> Byte::class.java
            "java.lang.Short"     -> Short::class.java
            "java.lang.Integer"   -> Int::class.java
            "java.lang.Float"     -> Float::class.java
            "java.lang.Long"      -> Long::class.java
            "java.lang.Double"    -> Double::class.java
            "java.lang.Void"      -> Void.TYPE
            else -> null
        } as Class<T>?
    }

/**
 * Returns a Java [Class] instance corresponding to the given [KClass] instance.
 * In case of primitive types it returns corresponding wrapper classes.
 */
public val <T : Any> KClass<T>.javaObjectType: Class<T>
    get() {
        val thisJClass = this.java
        if (!thisJClass.isPrimitive) return thisJClass as Class<T>

        return when (thisJClass.name) {
            "boolean" -> JavaLangBoolean::class.java
            "char"    -> JavaLangCharacter::class.java
            "byte"    -> JavaLangByte::class.java
            "short"   -> JavaLangShort::class.java
            "int"     -> JavaLangInteger::class.java
            "float"   -> JavaLangFloat::class.java
            "long"    -> JavaLangLong::class.java
            "double"  -> JavaLangDouble::class.java
            "void"    -> Void::class.java
            else -> thisJClass
        } as Class<T>
    }

/**
 * Returns the runtime Java class of this object.
 */
public inline val <T : Any> T.javaClass: Class<T>
    @Suppress("UsePropertyAccessSyntax")
    get() = (this as java.lang.Object).getClass() as Class<T>

@Deprecated("Use 'java' property to get Java class corresponding to this Kotlin class or cast this instance to Any if you really want to get the runtime Java class of this implementation of KClass.", ReplaceWith("(this as Any).javaClass"), level = DeprecationLevel.ERROR)
public inline val <T : Any> KClass<T>.javaClass: Class<KClass<T>>
    @JvmName("getRuntimeClassOfKClassInstance")
    @Suppress("UsePropertyAccessSyntax")
    get() = (this as java.lang.Object).getClass() as Class<KClass<T>>

/**
 * Checks if array can contain element of type [T].
 */
@Suppress("REIFIED_TYPE_PARAMETER_NO_INLINE")
public fun <reified T : Any> Array<*>.isArrayOf(): Boolean =
    T::class.java.isAssignableFrom(this::class.java.componentType)
