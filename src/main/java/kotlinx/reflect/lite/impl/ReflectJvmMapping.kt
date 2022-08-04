// Some logic copied from: https://github.com/JetBrains/kotlin/blob/26cdb2f928982dad7f0c9ab8e3bd509665d9d537/core/reflection.jvm/src/kotlin/reflect/jvm/ReflectJvmMapping.kt
package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.impl.*
import java.lang.reflect.*

// Kotlin reflection -> Java reflection

/**
 * Returns a Java [Field] instance corresponding to the backing field of the given property,
 * or `null` if the property has no backing field.
 */
val KProperty<*>.javaField: Field?
    get() = (this as? KPropertyImpl)?.descriptor?.javaField

/**
 * Returns a Java [Method] instance corresponding to the getter of the given property,
 * or `null` if the property has no getter, for example in case of a simple private `val` in a class.
 */
val KProperty<*>.javaGetter: Method?
    get() = getter.javaMethod

/**
 * Returns a Java [Method] instance corresponding to the setter of the given mutable property,
 * or `null` if the property has no setter, for example in case of a simple private `var` in a class.
 */
val KMutableProperty<*>.javaSetter: Method?
    get() = setter.javaMethod

/**
 * Returns a Java [Method] instance corresponding to the given Kotlin function,
 * or `null` if this function is a constructor or cannot be represented by a Java [Method].
 */
val KFunction<*>.javaMethod: Method?
    get() = (this as? KCallableImpl<*>)?.let {
        return it.descriptor.caller.member as? Method
    }

/**
 * Returns a Java [Constructor] instance corresponding to the given Kotlin function,
 * or `null` if this function is not a constructor or cannot be represented by a Java [Constructor].
 */
@Suppress("UNCHECKED_CAST")
val <T> KFunction<T>.javaConstructor: Constructor<T>?
    get() = (this as? KCallableImpl<*>)?.let {
        return it.descriptor.caller.member as? Constructor<T>
    }

/**
 * Returns a Java [Class] instance corresponding to the given [KClass] instance.
 */
public val <T> KClass<T>.java: Class<T>
    get() = (this as KClassImpl<T>).descriptor.jClass as Class<T>

/**
 * Returns a [KClass] instance representing the companion object of a given class,
 * or `null` if the class doesn't have a companion object.
 */
@SinceKotlin("1.1")
val KDeclarationContainer.companionObject: KClass<*>?
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
val KType.javaType: Type
    get() = this.computeJavaType()

/**
 * Returns a Java [Class] instance representing the primitive type corresponding to the given [KDeclarationContainer] if it exists.
 */
public val KDeclarationContainer.javaPrimitiveType: Class<*>?
    get() {
        val thisJClass = (this as KDeclarationContainerImpl).descriptor.jClass
        if (thisJClass.isPrimitive) return thisJClass

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
        }
    }

/**
 * Returns a Java [Class] instance corresponding to the given [KClass] instance. In case of primitive types it returns corresponding wrapper classes.
 */
public val KDeclarationContainer.javaObjectType: Class<*>
    get() {
        val thisJClass = (this as KDeclarationContainerImpl).descriptor.jClass
        if (!thisJClass.isPrimitive) return thisJClass

        return when (thisJClass.name) {
            "boolean" -> java.lang.Boolean::class.java
            "char"    -> Character::class.java
            "byte"    -> java.lang.Byte::class.java
            "short"   -> java.lang.Short::class.java
            "int"     -> Integer::class.java
            "float"   -> java.lang.Float::class.java
            "long"    -> java.lang.Long::class.java
            "double"  -> java.lang.Double::class.java
            "void"    -> Void::class.java
            else -> thisJClass
        }
    }

// Java reflection -> Kotlin reflection

/**
 * Returns a [KDeclarationContainer] instance representing a Kotlin class or package.
 */
val <T : Any> Class<T>.kotlinClass: KDeclarationContainer
    @JvmName("getKotlinClass")
    get() = ReflectionLiteImpl.loadClassMetadata(this)

private fun Member.getKPackage(): KDeclarationContainer? =
    when (declaringClass.getAnnotation(Metadata::class.java)?.kind) {
        2, 4, 5 -> KPackageImpl(PackageDescriptorImpl(declaringClass))
        else -> null
    }

/**
 * Returns a [KProperty] instance corresponding to the given Java [Field] instance,
 * or `null` if this field cannot be represented by a Kotlin property
 * (for example, if it is a synthetic field).
 */
val Field.kotlinProperty: KProperty<*>?
    get() {
        if (isSynthetic) return null
        val kotlinPackage = getKPackage()
        if (kotlinPackage != null) {
            return kotlinPackage.members.filterIsInstance<KProperty<*>>().firstOrNull { it.name == this.name }
        }
        return declaringClass.kotlinClass.members.filterIsInstance<KProperty<*>>().firstOrNull { it.name == this.name }
    }

/**
 * Returns a [KFunction] instance corresponding to the given Java [Method] instance,
 * or `null` if this method cannot be represented by a Kotlin function.
 */
val Method.kotlinFunction: KFunction<*>?
    get() {
        if (Modifier.isStatic(modifiers)) {
            val kotlinPackage = getKPackage()
            if (kotlinPackage != null) {
                return kotlinPackage.members.filterIsInstance<KFunction<*>>().firstOrNull { it.name == this.name }
            }

            // For static bridge method generated for a @JvmStatic function in the companion object, also try to find the latter
            val companion = declaringClass.kotlinClass.companionObject
            if (companion != null) {
                companion.members.filterIsInstance<KFunction<*>>().firstOrNull {
                    it.name == this.name
                }?.let { return it }
            }
        }
        return declaringClass.kotlinClass.members.filterIsInstance<KFunction<*>>().firstOrNull { it.name == this.name }
    }

/**
 * Returns a [KFunction] instance corresponding to the given Java [Constructor] instance,
 * or `null` if this constructor cannot be represented by a Kotlin function
 * (for example, if it is a synthetic constructor).
 */
val <T : Any> Constructor<T>.kotlinFunction: KFunction<T>?
    get() {
        return (declaringClass.kotlinClass as KClass<T>).constructors.firstOrNull { it.javaConstructor == this }
    }
