package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import java.util.ArrayList
import kotlin.coroutines.*
import kotlin.reflect.jvm.*
import kotlin.reflect.jvm.internal.*

internal abstract class KCallableImpl<out R>: KCallable<R> {
    abstract val descriptor: CallableDescriptor

    override val parameters: List<KParameter>
        get() {
            val descriptor = descriptor
            val result = mutableListOf<KParameter>()
            var index = 0
            // TODO bound receiver
            val instanceReceiver = descriptor.dispatchReceiverParameter

            if (instanceReceiver != null) {
                result.add(KParameterImpl(instanceReceiver, index++, KParameter.Kind.INSTANCE))
            }

            val extensionReceiver = descriptor.extensionReceiverParameter
            if (extensionReceiver != null) {
                result.add(KParameterImpl(extensionReceiver, index++, KParameter.Kind.EXTENSION_RECEIVER))
            }

            for (valueParameterDesc in descriptor.valueParameters) {
                result.add(KParameterImpl(valueParameterDesc, index++, KParameter.Kind.VALUE))
            }

            // Constructor parameters of Java annotations are not ordered in any way, we order them by name here to be more stable.
            // Note that positional call (via "call") is not allowed unless there's a single non-"value" parameter,
            // so the order of parameters of Java annotation constructors here can be arbitrary
            // TODO annotation constructors
            return result
        }

    override val visibility: KVisibility?
        get() = descriptor.visibility

    override val typeParameters: List<KTypeParameter>
        get() = descriptor.typeParameters.map { KTypeParameterImpl(it) }

    override val returnType: KType
        get() = KTypeImpl(descriptor.returnType)

    @Suppress("UNCHECKED_CAST")
    override fun call(vararg args: Any?): R {
        return descriptor.caller.call(args) as R
    }

    override fun callBy(args: Map<KParameter, Any?>): R {
        return callDefaultMethod(args, null)
    }

    // See ArgumentGenerator#generate
    internal fun callDefaultMethod(args: Map<KParameter, Any?>, continuationArgument: Continuation<*>?): R {
        val parameters = parameters
        val arguments = ArrayList<Any?>(parameters.size)
        var mask = 0
        val masks = ArrayList<Int>(1)
        var index = 0
        var anyOptional = false

        for (parameter in parameters) {
            if (index != 0 && index % Integer.SIZE == 0) {
                masks.add(mask)
                mask = 0
            }

            when {
                args.containsKey(parameter) -> {
                    arguments.add(args[parameter])
                }
                parameter.isOptional -> {
                    TODO("Support optional parameters")
                    anyOptional = true
                }
                parameter.isVararg -> {
                    TODO("Support vararg parameters")
                }
                else -> {
                    throw IllegalArgumentException("No argument provided for a required parameter: $parameter")
                }
            }

            if (parameter.kind == KParameter.Kind.VALUE) {
                index++
            }
        }

        if (continuationArgument != null) {
            arguments.add(continuationArgument)
        }

        if (!anyOptional) {
            return call(*arguments.toTypedArray())
        }

        masks.add(mask)

        val caller = descriptor.defaultCaller ?: throw KotlinReflectionInternalError("This callable does not support a default call: $descriptor")

        arguments.addAll(masks)

        // DefaultConstructorMarker or MethodHandle
        arguments.add(null)

        @Suppress("UNCHECKED_CAST")
        return caller.call(arguments.toTypedArray()) as R
    }
}
