// Copy of: https://github.com/JetBrains/kotlin/blob/master/core/reflection.jvm/src/kotlin/reflect/jvm/internal/calls/Caller.kt
package kotlinx.reflect.lite.calls

import java.lang.reflect.Member
import java.lang.reflect.Type

internal interface Caller<out M : Member?> {
    val member: M

    val returnType: Type

    val parameterTypes: List<Type>

    fun checkArguments(args: Array<*>) {
        if (arity != args.size) {
            throw IllegalArgumentException("Callable expects $arity arguments, but ${args.size} were provided.")
        }
    }

    fun call(args: Array<*>): Any?
}

internal val Caller<*>.arity: Int
    get() = parameterTypes.size
