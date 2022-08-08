@file:JvmName("KCallables")

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlin.coroutines.intrinsics.*

/**
 * Calls a callable in the current suspend context. If the callable is not a suspend function, behaves as [KCallable.call].
 * Otherwise, calls the suspend function with current continuation.
 */
@SinceKotlin("1.3")
suspend fun <R> KCallable<R>.callSuspend(vararg args: Any?): R {
    if (!this.isSuspend) return call(*args)
    if (this !is KFunction<*>) throw IllegalArgumentException("Cannot callSuspend on a property $this: suspend properties are not supported yet")
    val result = suspendCoroutineUninterceptedOrReturn<R> { call(*args, it) }
    // If suspend function returns Unit and tail-call, it might appear, that it returns not Unit,
    // see comment above replaceReturnsUnitMarkersWithPushingUnitOnStack for explanation.
    // In this case, return Unit manually.
    @Suppress("UNCHECKED_CAST")
    if (returnType.classifier == Unit::class && !returnType.isMarkedNullable) return (Unit as R)
    return result
}
