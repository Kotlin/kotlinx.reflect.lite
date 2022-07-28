// Copy of: https://github.com/JetBrains/kotlin/blob/master/core/reflection.jvm/src/kotlin/reflect/jvm/internal/calls/CallerImpl.kt
package kotlinx.reflect.lite.calls

import java.lang.reflect.Member
import java.lang.reflect.Modifier
import java.lang.reflect.Type
import java.lang.reflect.Constructor as ReflectConstructor
import java.lang.reflect.Field as ReflectField
import java.lang.reflect.Method as ReflectMethod

internal sealed class CallerImpl<out M : Member>(
    final override val member: M,
    final override val returnType: Type,
    val instanceClass: Class<*>?,
    valueParameterTypes: Array<Type>
) : Caller<M> {
    override val parameterTypes: List<Type> =
        instanceClass?.let { listOf(it, *valueParameterTypes) } ?: valueParameterTypes.toList()

    protected fun checkObjectInstance(obj: Any?) {
        if (obj == null || !member.declaringClass.isInstance(obj)) {
            throw IllegalArgumentException("An object member requires the object instance passed as the first argument.")
        }
    }

    class Constructor(constructor: ReflectConstructor<*>) : CallerImpl<ReflectConstructor<*>>(
        constructor,
        constructor.declaringClass,
        constructor.declaringClass.let { klass ->
            val outerClass = klass.declaringClass
            if (outerClass != null && !Modifier.isStatic(klass.modifiers)) outerClass else null
        },
        constructor.genericParameterTypes
    ) {
        override fun call(args: Array<*>): Any? {
            checkArguments(args)
            return member.newInstance(*args)
        }
    }

    sealed class Method(
        method: ReflectMethod,
        requiresInstance: Boolean = !Modifier.isStatic(method.modifiers),
        parameterTypes: Array<Type> = method.genericParameterTypes
    ) : CallerImpl<ReflectMethod>(
        method,
        method.genericReturnType,
        if (requiresInstance) method.declaringClass else null,
        parameterTypes
    ) {
        private val isVoidMethod = returnType == Void.TYPE

        protected fun callMethod(instance: Any?, args: Array<*>): Any? {
            val result = member.invoke(instance, *args)

            // If this is a Unit function, the method returns void, Method#invoke returns null, while we should return Unit
            return if (isVoidMethod) Unit else result
        }

        class Static(method: ReflectMethod) : Method(method) {
            override fun call(args: Array<*>): Any? {
                checkArguments(args)
                return callMethod(null, args)
            }
        }

        class Instance(method: ReflectMethod) : Method(method) {
            override fun call(args: Array<*>): Any? {
                checkArguments(args)
                return callMethod(args[0], args.dropFirst())
            }
        }

        class JvmStaticInObject(method: ReflectMethod) : Method(method, requiresInstance = true) {
            override fun call(args: Array<*>): Any? {
                checkArguments(args)
                checkObjectInstance(args.firstOrNull())
                return callMethod(null, args.dropFirst())
            }
        }
    }

    sealed class FieldGetter(
        field: ReflectField,
        requiresInstance: Boolean
    ) : CallerImpl<ReflectField>(
        field,
        field.genericType,
        if (requiresInstance) field.declaringClass else null,
        emptyArray()
    ) {
        override fun call(args: Array<*>): Any? {
            checkArguments(args)
            return member.get(if (instanceClass != null) args.first() else null)
        }

        class Static(field: ReflectField) : FieldGetter(field, requiresInstance = false)

        class Instance(field: ReflectField) : FieldGetter(field, requiresInstance = true)

        class JvmStaticInObject(field: ReflectField) : FieldGetter(field, requiresInstance = true) {
            override fun checkArguments(args: Array<*>) {
                super.checkArguments(args)
                checkObjectInstance(args.firstOrNull())
            }
        }
    }

    sealed class FieldSetter(
        field: ReflectField,
        private val notNull: Boolean,
        requiresInstance: Boolean
    ) : CallerImpl<ReflectField>(
        field,
        Void.TYPE,
        if (requiresInstance) field.declaringClass else null,
        arrayOf(field.genericType)
    ) {
        override fun checkArguments(args: Array<*>) {
            super.checkArguments(args)
            if (notNull && args.last() == null) {
                throw IllegalArgumentException("null is not allowed as a value for this property.")
            }
        }

        override fun call(args: Array<*>): Any? {
            checkArguments(args)
            return member.set(if (instanceClass != null) args.first() else null, args.last())
        }

        class Static(field: ReflectField, notNull: Boolean) : FieldSetter(field, notNull, requiresInstance = false)

        class Instance(field: ReflectField, notNull: Boolean) : FieldSetter(field, notNull, requiresInstance = true)

        class JvmStaticInObject(field: ReflectField, notNull: Boolean) : FieldSetter(field, notNull, requiresInstance = true) {
            override fun checkArguments(args: Array<*>) {
                super.checkArguments(args)
                checkObjectInstance(args.firstOrNull())
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        inline fun <reified T> Array<out T>.dropFirst(): Array<T> =
            if (size <= 1) emptyArray() else copyOfRange(1, size) as Array<T>

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T> Array<out T>.dropLast(): Array<T> =
            if (size <= 1) emptyArray() else copyOfRange(0, size - 1) as Array<T>

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T> Array<out T>.dropFirstAndLast(): Array<T> =
            if (size <= 2) emptyArray() else copyOfRange(1, size - 1) as Array<T>
    }
}
