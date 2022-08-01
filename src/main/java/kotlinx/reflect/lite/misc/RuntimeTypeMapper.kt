// Most logic copied from: https://github.com/JetBrains/kotlin/blob/master/core/reflection.jvm/src/kotlin/reflect/jvm/internal/RuntimeTypeMapper.kt
package kotlinx.reflect.lite.misc

import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.builtins.*
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.name.*

sealed class JvmFunctionSignature {
    abstract fun asString(): String

    class KotlinFunction(val signature: JvmMethodSignature) : JvmFunctionSignature() {
        private val _signature = signature.asString()

        val methodName: String get() = signature.name
        val methodDesc: String get() = signature.desc

        override fun asString(): String = _signature
    }

    class KotlinConstructor(val signature: JvmMethodSignature) : JvmFunctionSignature() {
        private val _signature = signature.asString()

        val constructorDesc: String get() = signature.desc

        override fun asString(): String = _signature
    }
}

internal sealed class JvmPropertySignature {

    class KotlinProperty(
        private val descriptor: PropertyDescriptorImpl,
        val fieldSignature: JvmFieldSignature?,
        val getterSignature: JvmMethodSignature?,
        val setterSignature: JvmMethodSignature?
    ) : JvmPropertySignature()

}

internal object RuntimeTypeMapper {
    private val JAVA_LANG_VOID = ClassId.topLevel(FqName("java.lang.Void"))

    fun mapJvmClassToKotlinClassId(klass: Class<*>): ClassId {
        if (klass.isArray) {
            klass.componentType.primitiveType?.let {
                return ClassId(FqName("kotlin"), it.arrayTypeName)
            }
            return ClassId.topLevel(StandardNames.FQ_NAMES.array)
        }

        if (klass == Void.TYPE) return JAVA_LANG_VOID

        klass.primitiveType?.let {
            return ClassId(StandardNames.BUILT_INS_PACKAGE_FQ_NAME, it.typeName)
        }

        val classId = klass.classId
        if (!classId.isLocal) {
            JavaToKotlinClassMap.mapJavaToKotlin(classId.asSingleFqName())?.let { return it }
        }

        return classId
    }

    private val Class<*>.primitiveType: PrimitiveType?
        get() = if (isPrimitive) JvmPrimitiveType.get(simpleName).primitiveType else null
}
