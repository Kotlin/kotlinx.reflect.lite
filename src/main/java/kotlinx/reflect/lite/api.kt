package kotlinx.reflect.lite

import kotlinx.reflect.lite.impl.ReflectionLiteImpl
import java.lang.reflect.Constructor
import java.lang.reflect.Method

interface ClassMetadata {
    fun getFunction(method: Method): FunctionMetadata?

    fun getConstructor(constructor: Constructor<*>): ConstructorMetadata?
}

interface CallableMetadata {
    val parameters: List<ParameterMetadata>
}

interface ConstructorMetadata : CallableMetadata

interface FunctionMetadata : CallableMetadata

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
