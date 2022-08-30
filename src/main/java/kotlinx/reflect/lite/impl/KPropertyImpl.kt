/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

internal abstract class KPropertyImpl<out T>(
    override val descriptor: PropertyDescriptor
) : KCallableImpl<T>(), KProperty<T> {
    override val name: String
        get() = descriptor.name

    override val isConst: Boolean
        get() = descriptor.isConst
    override val isLateInit: Boolean
        get() = descriptor.isLateInit
    override val isSuspend: Boolean
        get() = false

    override fun call(vararg args: Any?): T = getter.call(args)

    abstract override val getter: KProperty.Getter<T>

    override fun equals(other: Any?): Boolean =
        other is KPropertyImpl<*> && descriptor == other.descriptor

    override fun hashCode(): Int = descriptor.hashCode()

    abstract class Accessor<out PropertyType, out ReturnType> : KCallableImpl<ReturnType>(), KProperty.Accessor<PropertyType>, KFunction<ReturnType> {
        abstract override val descriptor: PropertyAccessorDescriptor

        abstract override val property: KPropertyImpl<PropertyType>

        override val isInline: Boolean get() = descriptor.isInline
        override val isExternal: Boolean get() = descriptor.isExternal
        override val isOperator: Boolean get() = descriptor.isOperator
        override val isInfix: Boolean get() = descriptor.isInfix
        override val isSuspend: Boolean get() = descriptor.isSuspend
        override val isFinal: Boolean get() = descriptor.isFinal
        override val isAbstract: Boolean get() = descriptor.isAbstract
        override val isOpen: Boolean get() = descriptor.isOpen // todo check inherrited proeprties
    }

    abstract class Getter<out V> : Accessor<V, V>(), KProperty.Getter<V> {
        override val name: String get() = "<get-${property.name}>"

        override val descriptor: PropertyGetterDescriptor
            get() = property.descriptor.getter ?: TODO("create default property getter")

        override fun toString(): String = "getter of $property"

        override fun equals(other: Any?): Boolean =
            other is Getter<*> && property == other.property

        override fun hashCode(): Int = property.hashCode()
    }

    abstract class Setter<V> : Accessor<V, Unit>(), KMutableProperty.Setter<V> {
        override val name: String get() = "<set-${property.name}>"

        override val descriptor: PropertySetterDescriptor
            get() = property.descriptor.setter ?: TODO("create default property setter")

        override fun toString(): String = "setter of $property"

        override fun equals(other: Any?): Boolean =
            other is Setter<*> && property == other.property

        override fun hashCode(): Int = property.hashCode()
    }
}

internal open class KProperty0Impl<out V>(
    override val descriptor: PropertyDescriptor
): KPropertyImpl<V>(descriptor), KProperty0<V> {
    override val getter: KProperty0.Getter<V>
        get() = Getter(this)

    override fun get(): V = getter.call()

    override fun invoke(): V = get()

    class Getter<out R>(
        override val property: KProperty0Impl<R>
    ) : KPropertyImpl.Getter<R>(), KProperty0.Getter<R> {
        override fun invoke(): R = property.get()
    }
}

internal open class KMutableProperty0Impl<V>(
    override val descriptor: PropertyDescriptor
): KProperty0Impl<V>(descriptor), KMutableProperty0<V> {

    override val setter: Setter<V>
        get() = Setter(this)

    override fun set(value: V) = setter.call(value)

    class Setter<R>(override val property: KMutableProperty0Impl<R>) : KPropertyImpl.Setter<R>(), KMutableProperty0.Setter<R> {
        override fun invoke(value: R): Unit = property.set(value)
    }
}

internal open class KProperty1Impl<T, out V>(
    override val descriptor: PropertyDescriptor
): KPropertyImpl<V>(descriptor), KProperty1<T, V> {
    override val getter: KProperty1.Getter<T, V>
        get() = Getter(this)

    override fun get(receiver: T): V = getter.call(receiver)

    override fun invoke(receiver: T): V = get(receiver)

    class Getter<T, out V>(override val property: KProperty1Impl<T, V>) :KPropertyImpl.Getter<V>(), KProperty1.Getter<T, V> {
        override fun invoke(receiver: T): V = property.get(receiver)
    }
}

internal open class KMutableProperty1Impl<T, V>(
    override val descriptor: PropertyDescriptor
): KProperty1Impl<T, V>(descriptor), KMutableProperty1<T, V> {

    override val setter: Setter<T, V>
        get() = Setter(this)

    override fun set(receiver: T, value: V) = setter.call(receiver, value)

    class Setter<T, V>(override val property: KMutableProperty1Impl<T, V>) : KPropertyImpl.Setter<V>(), KMutableProperty1.Setter<T, V> {
        override fun invoke(receiver: T, value: V): Unit = property.set(receiver, value)
    }
}

internal open class KProperty2Impl<D, E, out V>(
    override val descriptor: PropertyDescriptor
): KPropertyImpl<V>(descriptor), KProperty2<D, E, V> {
    override val getter: Getter<D, E, V>
        get() = Getter(this)

    override fun get(receiver1: D, receiver2: E): V = getter.call(receiver1, receiver2)

    override fun invoke(receiver1: D, receiver2: E): V = get(receiver1, receiver2)

    class Getter<D, E, out V>(override val property: KProperty2Impl<D, E, V>) :KPropertyImpl.Getter<V>(), KProperty2.Getter<D, E, V> {
        override fun invoke(receiver1: D, receiver2: E): V = property.get(receiver1, receiver2)
    }
}

internal open class KMutableProperty2Impl<D, E, V>(
    override val descriptor: PropertyDescriptor
): KProperty2Impl<D, E, V>(descriptor), KMutableProperty2<D, E, V> {

    override val setter: Setter<D, E, V>
        get() = Setter(this)

    override fun set(receiver1: D, receiver2: E, value: V) = setter.call(receiver1, receiver2, value)

    class Setter<D, E, V>(override val property: KMutableProperty2Impl<D, E, V>) : KPropertyImpl.Setter<V>(), KMutableProperty2.Setter<D, E, V> {
        override fun invoke(receiver1: D, receiver2: E, value: V): Unit = property.set(receiver1, receiver2, value)
    }
}
