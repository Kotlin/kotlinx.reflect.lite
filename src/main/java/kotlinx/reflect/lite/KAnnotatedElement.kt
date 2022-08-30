package kotlinx.reflect.lite

import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.misc.notImplemented

/**
 * Represents an annotated element and allows to obtain its annotations.
 * See the [Kotlin language documentation](https://kotlinlang.org/docs/reference/annotations.html)
 * for more information.
 */
public interface KAnnotatedElement {
    /**
     * Annotations which are present on this element.
     */
    private val annotations: List<Annotation>
        get() = notImplemented()
}
