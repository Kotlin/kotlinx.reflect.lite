/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.jvm.*
import java.lang.reflect.*
import java.util.ArrayList
import kotlin.coroutines.*

internal abstract class KCallableImpl<out R>: KCallable<R> {
    abstract val descriptor: CallableDescriptor

    override val parameters: List<KParameter> by lazy {
        val descriptor = descriptor
        val result = mutableListOf<KParameter>()
        var index = 0
        val instanceReceiver = descriptor.dispatchReceiverParameter

        if (instanceReceiver != null) {
            result.add(KParameterImpl(instanceReceiver, index++, KParameter.Kind.INSTANCE, descriptor))
        }

        val extensionReceiver = descriptor.extensionReceiverParameter
        if (extensionReceiver != null) {
            result.add(KParameterImpl(extensionReceiver, index++, KParameter.Kind.EXTENSION_RECEIVER, descriptor))
        }

        for (valueParameterDesc in descriptor.valueParameters) {
            result.add(KParameterImpl(valueParameterDesc, index++, KParameter.Kind.VALUE, descriptor))
        }
        result
    }

    override val visibility: KVisibility?
        get() = descriptor.visibility

    override val typeParameters: List<KTypeParameter>
        get() = descriptor.typeParameters.map { KTypeParameterImpl(it) }

    override val returnType: KType
        get() = KTypeImpl(descriptor.returnType) {
            extractContinuationArgument() ?: descriptor.caller.returnType
        }

    override val isAbstract: Boolean
        get() = descriptor.isAbstract
    override val isFinal: Boolean
        get() = descriptor.isFinal
    override val isOpen: Boolean
        get() = descriptor.isOpen

    @Suppress("UNCHECKED_CAST")
    override fun call(vararg args: Any?): R {
        return descriptor.caller.call(args) as R
    }

    override fun callBy(args: Map<KParameter, Any?>): R {
        return callDefaultMethod(args, null)
    }

    // Logic from: https://github.com/JetBrains/kotlin/blob/ea836fd46a1fef07d77c96f9d7e8d7807f793453/core/reflection.jvm/src/kotlin/reflect/jvm/internal/KCallableImpl.kt#L116
    private fun callDefaultMethod(args: Map<KParameter, Any?>, continuationArgument: Continuation<*>?): R {
        val parameters = parameters
        // TODO here we can avoid extra arguments copy by allocating an array of precise size if
        // varargs are not present and win some performance
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
                    arguments.add(defaultPrimitiveValue(parameter.type.javaType))
                    mask = mask or (1 shl (index % Integer.SIZE))
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

    private fun extractContinuationArgument(): Type? {
        if ((descriptor as? FunctionDescriptor)?.isSuspend == true) {
            // kotlin.coroutines.Continuation<? super java.lang.String>
            val continuationType = descriptor.caller.parameterTypes.lastOrNull() as? ParameterizedType
            if (continuationType?.rawType == Continuation::class.java) {
                // ? super java.lang.String
                val wildcard = continuationType.actualTypeArguments.single() as? WildcardType
                // java.lang.String
                return wildcard?.lowerBounds?.first()
            }
        }

        return null
    }
}
