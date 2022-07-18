package kotlinx.reflect.lite.misc

import kotlinx.reflect.lite.name.*
import java.lang.reflect.Array
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

val Class<*>.safeClassLoader: ClassLoader
    get() = classLoader ?: ClassLoader.getSystemClassLoader()

private val PRIMITIVE_CLASSES =
    listOf(Boolean::class, Byte::class, Char::class, Double::class, Float::class, Int::class, Long::class, Short::class)
private val WRAPPER_TO_PRIMITIVE = PRIMITIVE_CLASSES.map { it.javaObjectType to it.javaPrimitiveType }.toMap()
private val PRIMITIVE_TO_WRAPPER = PRIMITIVE_CLASSES.map { it.javaPrimitiveType to it.javaObjectType }.toMap()

val Class<*>.primitiveByWrapper: Class<*>?
    get() = WRAPPER_TO_PRIMITIVE[this]

val Class<*>.wrapperByPrimitive: Class<*>?
    get() = PRIMITIVE_TO_WRAPPER[this]

private val FUNCTION_CLASSES =
    listOf(
        Function0::class.java, Function1::class.java, Function2::class.java, Function3::class.java, Function4::class.java,
        Function5::class.java, Function6::class.java, Function7::class.java, Function8::class.java, Function9::class.java,
        Function10::class.java, Function11::class.java, Function12::class.java, Function13::class.java, Function14::class.java,
        Function15::class.java, Function16::class.java, Function17::class.java, Function18::class.java, Function19::class.java,
        Function20::class.java, Function21::class.java, Function22::class.java
    ).mapIndexed { i, clazz -> clazz to i }.toMap()

val Class<*>.functionClassArity: Int?
    get() = FUNCTION_CLASSES[this]

/**
 * NOTE: does not perform a Java -> Kotlin mapping. If this is not expected, consider using KClassImpl#classId instead TODO
 */
val Class<*>.classId: ClassId
    get() = when {
        isPrimitive -> throw IllegalArgumentException("Can't compute ClassId for primitive type: $this")
        isArray -> throw IllegalArgumentException("Can't compute ClassId for array type: $this")
        enclosingMethod != null || enclosingConstructor != null || simpleName.isEmpty() -> {
            val fqName = FqName(name)
            ClassId(fqName.parent(), FqName(fqName.shortName()), /* local = */ true)
        }
        else -> declaringClass?.classId?.createNestedClassId(simpleName) ?: ClassId.topLevel(FqName(name))
    }

val Class<*>.desc: String
    get() {
        if (this == Void.TYPE) return "V"
        // This is a clever exploitation of a format returned by Class.getName(): for arrays, it's almost an internal name,
        // but with '.' instead of '/'
        return createArrayType().name.substring(1).replace('.', '/')
    }

fun Class<*>.createArrayType(): Class<*> =
    Array.newInstance(this, 0)::class.java

/**
 * @return all arguments of a parameterized type, including those of outer classes in case this type represents an inner generic.
 * The returned list starts with the arguments to the innermost class, then continues with those of its outer class, and so on.
 * For example, for the type `Outer<A, B>.Inner<C, D>` the result would be `[C, D, A, B]`.
 */
val Type.parameterizedTypeArguments: List<Type>
    get() {
        if (this !is ParameterizedType) return emptyList()
        if (ownerType == null) return actualTypeArguments.toList()

        return generateSequence(this) { it.ownerType as? ParameterizedType }.flatMap { it.actualTypeArguments.asSequence() }.toList()
    }

fun ClassLoader.tryLoadClass(fqName: String) =
    try {
        Class.forName(fqName, false, this)
    } catch (e: ClassNotFoundException) {
        null
    }