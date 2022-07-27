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

// Java reflection -> Kotlin reflection

val <T : Any> Class<T>.kotlinClass: KDeclarationContainer
    @JvmName("getKotlinClass")
    get() = ReflectionLiteImpl.loadClassMetadata(this)

private fun Member.getKPackage(): KDeclarationContainer? =
    when (declaringClass.getAnnotation(Metadata::class.java)?.kind) {
        2, 4, 5 -> KPackageImpl(PackageDescriptorImpl(declaringClass))
        else -> null
    }

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
 * Returns a [KProperty] instance corresponding to the given Java [Field] instance,
 * or `null` if this field cannot be represented by a Kotlin property
 * (for example, if it is a synthetic field).
 */
// TODO renamed for now for resolution
val Field.kotlinLiteProperty: KProperty<*>?
    get() {
        if (isSynthetic) return null

        // TODO: optimize (search by name)

        val kotlinPackage = getKPackage()
        if (kotlinPackage != null) {
            return kotlinPackage.members.filterIsInstance<KProperty<*>>().firstOrNull { it.name == this.name }
        }
        // TODO comapare by javaField: { it.javaField == this }
        return declaringClass.kotlinClass.members.filterIsInstance<KProperty<*>>().firstOrNull { it.name == this.name }
    }

/**
 * Returns a [KFunction] instance corresponding to the given Java [Method] instance,
 * or `null` if this method cannot be represented by a Kotlin function.
 */
val Method.kotlinLiteFunction: KFunction<*>?
    get() {
        if (Modifier.isStatic(modifiers)) {
            val kotlinPackage = getKPackage()
            if (kotlinPackage != null) {
                // TODO comapare by javaMethod: { it.javaField == this }
                return kotlinPackage.members.filterIsInstance<KFunction<*>>().firstOrNull { it.name == this.name }
            }

            // For static bridge method generated for a @JvmStatic function in the companion object, also try to find the latter
            val companion = declaringClass.kotlinClass.companionObject
            if (companion != null) {
                companion.members.filterIsInstance<KFunction<*>>().firstOrNull {
                    // TODO comapare by javaMethod: { it.javaField == this }
                    //val m = it.javaMethod
//                    m != null && m.name == this.name &&
//                            m.parameterTypes!!.contentEquals(this.parameterTypes) && m.returnType == this.returnType
                    it.name == this.name
                }?.let { return it }
            }
        }
        return declaringClass.kotlinClass.members.filterIsInstance<KFunction<*>>().firstOrNull { it.name == this.name }
    }
