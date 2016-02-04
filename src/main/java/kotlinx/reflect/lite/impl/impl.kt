package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import org.jetbrains.kotlin.serialization.ProtoBuf
import org.jetbrains.kotlin.serialization.deserialization.NameResolver
import org.jetbrains.kotlin.serialization.deserialization.TypeTable
import org.jetbrains.kotlin.serialization.jvm.JvmProtoBufUtil
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import kotlin.jvm.internal.KotlinClass
import java.lang.reflect.Array as ReflectArray

internal class ClassMetadataImpl(
        private val proto: ProtoBuf.Class,
        private val nameResolver: NameResolver
) : ClassMetadata {
    private val typeTable = TypeTable(proto.typeTable)

    private val functions: Map<String, ProtoBuf.Function> by lazySoft {
        proto.functionList.map { function ->
            JvmProtoBufUtil.getJvmMethodSignature(function, nameResolver, typeTable)
                    ?.let { it to function }
        }.filterNotNull().toMap()
    }

    private val constructors: Map<String, ProtoBuf.Constructor> by lazySoft {
        proto.constructorList.map { constructor ->
            JvmProtoBufUtil.getJvmConstructorSignature(constructor, nameResolver, typeTable)
                    ?.let { it to constructor }
        }.filterNotNull().toMap()
    }

    override fun getFunction(method: Method): FunctionMetadata? {
        return functions[signature(method.name, method.parameterTypes, method.returnType)]
                ?.let { FunctionMetadataImpl(it, nameResolver) }
    }

    override fun getConstructor(constructor: Constructor<*>): ConstructorMetadata? {
        return constructors[signature("<init>", constructor.parameterTypes, Void.TYPE)]
                ?.let { ConstructorMetadataImpl(it, nameResolver) }
    }

    private fun signature(name: String, parameterTypes: Array<Class<*>>, returnType: Class<*>): String {
        return buildString {
            append(name)
            parameterTypes.joinTo(this, separator = "", prefix = "(", postfix = ")", transform = Class<*>::desc)
            append(returnType.desc())
        }
    }
}

internal abstract class CallableMetadataImpl : CallableMetadata

internal class FunctionMetadataImpl(
        private val proto: ProtoBuf.Function,
        private val nameResolver: NameResolver
) : CallableMetadataImpl(), FunctionMetadata {
    override val parameters: List<ParameterMetadata>
        get() = proto.valueParameterList.map { ParameterMetadataImpl(it, nameResolver) }
}

internal class ConstructorMetadataImpl(
        private val proto: ProtoBuf.Constructor,
        private val nameResolver: NameResolver
) : CallableMetadataImpl(), ConstructorMetadata {
    override val parameters: List<ParameterMetadata>
        get() = proto.valueParameterList.map { ParameterMetadataImpl(it, nameResolver) }
}

internal class ParameterMetadataImpl(
        private val proto: ProtoBuf.ValueParameter,
        private val nameResolver: NameResolver
) : ParameterMetadata {
    override val name: String?
        get() = nameResolver.getString(proto.name)

    override val type: TypeMetadata
        get() = TypeMetadataImpl(proto.type, nameResolver)
}

internal class TypeMetadataImpl(
        private val proto: ProtoBuf.Type,
        private val nameResolver: NameResolver
) : TypeMetadata {
    override val isNullable: Boolean
        get() = proto.nullable
}


internal object ReflectionLiteImpl {
    fun loadClassMetadata(klass: Class<*>): ClassMetadata? {
        val annotation = klass.declaredAnnotations.singleOrNull { it.annotationClass.java == KotlinClass::class.java } as KotlinClass? ?: return null
        val (nameResolver, classProto) = JvmProtoBufUtil.readClassDataFrom(annotation.data, annotation.strings)
        return ClassMetadataImpl(classProto, nameResolver)
    }
}
