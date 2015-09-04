package kotlinx.reflect.lite.impl

import java.lang.ref.SoftReference

internal fun lazySoft<T>(initializer: () -> T) = LazySoftImpl(initializer)

@suppress("UNCHECKED_CAST")
private class LazySoftImpl<out T>(private val initializer: () -> T) {
    private volatile var value: Any? = null

    fun get(thisRef: Any?, property: PropertyMetadata): T {
        (value as? SoftReference<T>)?.get()?.let { return it }

        return initializer().apply { value = SoftReference(this) }
    }
}
