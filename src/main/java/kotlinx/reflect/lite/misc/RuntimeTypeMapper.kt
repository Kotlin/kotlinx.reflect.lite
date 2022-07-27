// Most logic copied from: https://github.com/JetBrains/kotlin/blob/master/core/reflection.jvm/src/kotlin/reflect/jvm/internal/RuntimeTypeMapper.kt
package kotlinx.reflect.lite.misc

import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.builtins.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.FunctionDescriptor
import kotlinx.reflect.lite.descriptors.PropertyDescriptor
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.descriptors.impl.FunctionDescriptorImpl
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.impl.FakeOverridePropertyDescriptor
import kotlinx.reflect.lite.name.*
import java.lang.reflect.*

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

    class JavaMethod(val method: Method) : JvmFunctionSignature() {
        override fun asString(): String = method.signature
    }

    class JavaConstructor(val constructor: Constructor<*>) : JvmFunctionSignature() {
        override fun asString(): String =
            constructor.parameterTypes.joinToString(separator = "", prefix = "<init>(", postfix = ")V") { it.desc }
    }

    class FakeJavaAnnotationConstructor(val jClass: Class<*>) : JvmFunctionSignature() {
        // Java annotations do not impose any order of methods inside them, so we consider them lexicographic here for stability
        val methods = jClass.declaredMethods.sortedBy { it.name }

        override fun asString(): String =
            methods.joinToString(separator = "", prefix = "<init>(", postfix = ")V") { it.returnType.desc }
    }
}

internal sealed class JvmPropertySignature {
    /**
     * Returns the JVM signature of the getter of this property. In case the property doesn't have a getter,
     * constructs the signature of its imaginary default getter. See CallableReference#getSignature for more information
     */
    abstract fun asString(): String

    class KotlinProperty(
        private val descriptor: PropertyDescriptorImpl,
        val fieldSignature: JvmFieldSignature?,
        val getterSignature: JvmMethodSignature?,
        val setterSignature: JvmMethodSignature?
    ) : JvmPropertySignature() {
        private val string: String = getterSignature?.asString() ?: run {
            val (name, desc) = fieldSignature ?: throw KotlinReflectionInternalError("No field signature for property: $descriptor")
            JvmAbi.getterName(name) + getManglingSuffix() + "()" + desc
        }

        private fun getManglingSuffix(): String {
            val containingClass = descriptor.containingClass
            if (descriptor.visibility == KVisibility.INTERNAL && containingClass is ClassDescriptorImpl) {
                val moduleName = containingClass.kmClass.moduleName ?: "main"
                return "$" + sanitizeAsJavaIdentifier(moduleName)
            }
            if (descriptor.visibility == KVisibility.PRIVATE && containingClass == null) {
                //(descriptor.container as KPackageImpl)
                // TODO
/*
                val packagePartSource = (descriptor as DeserializedPropertyDescriptor).containerSource
                if (packagePartSource is JvmPackagePartSource && packagePartSource.facadeClassName != null) {
                    return "$" + packagePartSource.simpleName.asString()
                }
*/
            }
            return ""
        }

        override fun asString(): String = string
    }

    class JavaMethodProperty(val getterMethod: Method, val setterMethod: Method?) : JvmPropertySignature() {
        override fun asString(): String = getterMethod.signature
    }

    class JavaField(val field: Field) : JvmPropertySignature() {
        override fun asString(): String =
            JvmAbi.getterName(field.name) + "()" + field.type.desc
    }

    class MappedKotlinProperty(
        val getterSignature: JvmFunctionSignature.KotlinFunction,
        val setterSignature: JvmFunctionSignature.KotlinFunction?
    ) : JvmPropertySignature() {
        override fun asString(): String = getterSignature.asString()
    }
}

private val Method.signature: String
    get() = name +
            parameterTypes.joinToString(separator = "", prefix = "(", postfix = ")") { it.desc } +
            returnType.desc

internal object RuntimeTypeMapper {
    private val JAVA_LANG_VOID = ClassId.topLevel(FqName("java.lang.Void"))

    fun mapSignature(funcDesc: FunctionDescriptor): JvmFunctionSignature {
        if (funcDesc is FunctionDescriptorImpl) {
            return JvmFunctionSignature.KotlinFunction(funcDesc.kmFunction.signature ?: error("No signature for ${funcDesc.kmFunction}"))
        }
        if (funcDesc is ConstructorDescriptorImpl) {
            return JvmFunctionSignature.KotlinConstructor(funcDesc.kmCons.signature ?: error("No constructor signature for ${funcDesc.kmCons}"))
        }
//        if (function is PropertyGetterDescriptorImpl) {
//            return JvmFunctionSignature.KotlinFunction(function.property.property.getterSignature ?: error("No getter signature for ${function.render()}"))
//        }
//        if (function is PropertySetterDescriptorImpl) {
//            return JvmFunctionSignature.KotlinFunction(function.property.property.setterSignature ?: error("No setter signature for ${function.render()}"))
//        }
//        if (function is FakeOverrideFunctionDescriptor) {
//            return mapSignature(function.overriddenFunctions.first())
//        }

         throw KotlinReflectionInternalError("Unknown origin of $funcDesc (${funcDesc.javaClass})")
    }

    fun mapPropertySignature(possiblyOverriddenProperty: PropertyDescriptor): JvmPropertySignature {
        val property = possiblyOverriddenProperty

        if (property is PropertyDescriptorImpl) {
            return JvmPropertySignature.KotlinProperty(
                property,
                property.kmProperty.fieldSignature,
                property.kmProperty.getterSignature,
                property.kmProperty.setterSignature
            )
        }

        if (property is FakeOverridePropertyDescriptor) {
            return mapPropertySignature(property.overriddenProperties.first())
        }
        TODO()
    }


    private fun isKnownBuiltInFunction(descriptor: FunctionDescriptor): Boolean {
        // if (DescriptorFactory.isEnumValueOfMethod(descriptor) || DescriptorFactory.isEnumValuesMethod(descriptor)) return true

        // if (descriptor.name == CloneableClassScope.CLONE_NAME && descriptor.valueParameters.isEmpty()) return true

        return false
    }

//    private fun mapName(descriptor: CallableMemberDescriptor): String =
//        descriptor.name

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
