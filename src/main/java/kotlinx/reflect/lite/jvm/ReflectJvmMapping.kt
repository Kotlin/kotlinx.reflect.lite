@file:JvmName("ReflectJvmMapping")

// Some logic copied from: https://github.com/JetBrains/kotlin/blob/26cdb2f928982dad7f0c9ab8e3bd509665d9d537/core/reflection.jvm/src/kotlin/reflect/jvm/ReflectJvmMapping.kt
package kotlinx.reflect.lite.jvm

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import java.lang.reflect.*

// Kotlin reflection -> Java reflection

/**
 * Returns a Java [Field] instance corresponding to the backing field of the given property,
 * or `null` if the property has no backing field.
 */
public val KProperty<*>.javaField: Field?
    get() = (this as? KPropertyImpl)?.descriptor?.javaField

/**
 * Returns a Java [Method] instance corresponding to the getter of the given property,
 * or `null` if the property has no getter, for example in case of a simple private `val` in a class.
 */
public val KProperty<*>.javaGetter: Method?
    get() = getter.javaMethod

/**
 * Returns a Java [Method] instance corresponding to the setter of the given mutable property,
 * or `null` if the property has no setter, for example in case of a simple private `var` in a class.
 */
public val KMutableProperty<*>.javaSetter: Method?
    get() = setter.javaMethod

/**
 * Returns a Java [Method] instance corresponding to the given Kotlin function,
 * or `null` if this function is a constructor or cannot be represented by a Java [Method].
 */
public val KFunction<*>.javaMethod: Method?
    get() = (this as? KCallableImpl<*>)?.let {
        return it.descriptor.caller.member as? Method
    }

/**
 * Returns a Java [Constructor] instance corresponding to the given Kotlin function,
 * or `null` if this function is not a constructor or cannot be represented by a Java [Constructor].
 */
@Suppress("UNCHECKED_CAST")
public val <T> KFunction<T>.javaConstructor: Constructor<T>?
    get() = (this as? KCallableImpl<*>)?.let {
        return it.descriptor.caller.member as? Constructor<T>
    }

// Java reflection -> Kotlin reflection

/**
 * Returns a [KClass] instance representing the companion object of a given class,
 * or `null` if the class doesn't have a companion object.
 */
@SinceKotlin("1.1")
public val KDeclarationContainer.companionObject: KClass<*>?
    get() {
        if (this !is KClass<*>) return null
        return nestedClasses.firstOrNull {
            (it as KClassImpl<*>).descriptor.isCompanion
        }
    }

/**
 * Returns a Java [Type] instance corresponding to the given Kotlin type.
 * Note that one Kotlin type may correspond to different JVM types depending on where it appears. For example, [Unit] corresponds to
 * the JVM class [Unit] when it's the type of a parameter, or to `void` when it's the return type of a function.
 */
public val KType.javaType: Type
    get() = (this as KTypeImpl).javaType ?: this.computeJavaType()

/**
 * Returns a [KProperty] instance corresponding to the given Java [Field] instance,
 * or `null` if this field cannot be represented by a Kotlin property
 * (for example, if it is a synthetic field).
 */
public val Field.kotlinProperty: KProperty<*>?
    get() = if (isSynthetic) null else declaringClass.kDeclarationContainer.getPropertyByName(this.name)

/**
 * Returns a [KFunction] instance corresponding to the given Java [Method] instance,
 * or `null` if this method cannot be represented by a Kotlin function.
 */
public val Method.kotlinFunction: KFunction<*>?
    get() {
        if (Modifier.isStatic(modifiers)) {
            // For static bridge method generated for a @JvmStatic function in the companion object, also try to find the latter
            val companion = declaringClass.kDeclarationContainer.companionObject
            if (companion != null) {
                companion.members.filterIsInstance<KFunction<*>>().firstOrNull {
                    it.name == this.name
                }?.let { return it }
            }
        }
        return declaringClass.kDeclarationContainer.getFunctionByName(this.name)
    }

/**
 * Returns a [KFunction] instance corresponding to the given Java [Constructor] instance,
 * or `null` if this constructor cannot be represented by a Kotlin function
 * (for example, if it is a synthetic constructor).
 */
public val <T : Any> Constructor<T>.kotlinFunction: KFunction<T>?
    get() {
        return (declaringClass.kotlin).constructors.firstOrNull { it.javaConstructor == this }
    }

/**
 * Returns a [KDeclarationContainer] instance representing a Kotlin class or package.
 */
internal val <T : Any> Class<T>.kDeclarationContainer: KDeclarationContainer
    get() = ReflectionLiteImpl.createKotlinDeclarationContainer(this)

private fun KDeclarationContainer.getPropertyByName(name: String) =
    members.filterIsInstance<KProperty<*>>().firstOrNull { it.name == name }

private fun KDeclarationContainer.getFunctionByName(name: String) =
    members.filterIsInstance<KFunction<*>>().firstOrNull { it.name == name }
