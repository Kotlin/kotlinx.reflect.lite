package kotlinx.reflect.lite.impl

import java.lang.ref.SoftReference
import java.lang.reflect.Array
import kotlin.reflect.KProperty

internal fun <T> lazySoft(initializer: () -> T) = LazySoftImpl(initializer)

@Suppress("UNCHECKED_CAST")
internal class LazySoftImpl<out T>(private val initializer: () -> T) {
    @Volatile private var value: Any? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        (value as? SoftReference<T>)?.get()?.let { return it }

        return initializer().apply { value = SoftReference(this) }
    }
}

fun Class<*>.desc(): String {
    if (this == Void.TYPE) return "V"
    return Array.newInstance(this, 0).javaClass.name.substring(1).replace('.', '/')
}
