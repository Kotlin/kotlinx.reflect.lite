// Copy of: https://github.com/JetBrains/kotlin/blob/master/core/reflection.jvm/src/kotlin/reflect/jvm/internal/calls/ThrowingCaller.kt
package kotlinx.reflect.lite.calls

import java.lang.reflect.Type

internal object ThrowingCaller : Caller<Nothing?> {
    override val member: Nothing?
        get() = null

    override val parameterTypes: List<Type>
        get() = emptyList()

    override val returnType: Type
        get() = Void.TYPE

    override fun call(args: Array<*>): Any? {
        throw UnsupportedOperationException("call/callBy are not supported for this declaration.")
    }
}
