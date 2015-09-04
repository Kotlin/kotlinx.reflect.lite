package kotlinx.reflect.lite

import kotlinx.reflect.lite.impl.ReflectionLiteImpl
import java.lang.reflect.Method

interface ClassMetadata {
    fun getFunction(method: Method): FunctionMetadata?
}

interface FunctionMetadata {
    val parameters: List<ParameterMetadata>
}

interface ParameterMetadata {
    val name: String?

    val type: TypeMetadata
}

interface TypeMetadata {
    val isNullable: Boolean
}



object ReflectionLite {
    fun loadClassMetadata(klass: Class<*>): ClassMetadata? {
        return ReflectionLiteImpl.loadClassMetadata(klass)
    }
}
