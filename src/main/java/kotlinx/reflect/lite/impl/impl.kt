package kotlinx.reflect.lite.impl

import com.google.protobuf.ExtensionRegistryLite
import kotlinx.reflect.lite.*
import org.jetbrains.kotlin.serialization.ProtoBuf
import org.jetbrains.kotlin.serialization.jvm.BitEncoding
import org.jetbrains.kotlin.serialization.jvm.JvmProtoBuf
import java.io.ByteArrayInputStream
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import kotlin.jvm.internal.KotlinClass
import java.lang.reflect.Array as ReflectArray

internal class ClassMetadataImpl(
        private val proto: ProtoBuf.Class,
        private val nameResolver: StringTable
) : ClassMetadata {
    private val map: Map<String, ProtoBuf.Callable> by lazySoft {
        fun JvmProtoBuf.JvmType.typeDesc(): String {
            return "[".repeat(arrayDimension) +
                    if (hasPrimitiveType())
                        "VZCBSIFJD"[primitiveType.ordinal]
                    else
                        "L" + nameResolver.getInternalName(classFqName) + ";"
        }

        val members =
                proto.memberList +
                proto.secondaryConstructorList +
                listOf(proto.primaryConstructor?.data).filterNotNull()

        members.map { callable ->
            callable.getExtension(JvmProtoBuf.methodSignature)?.let { methodSignature ->
                val string = buildString {
                    append(nameResolver.getString(methodSignature.name))
                    methodSignature.parameterTypeList.joinTo(this, separator = "", prefix = "(", postfix = ")", transform = JvmProtoBuf.JvmType::typeDesc)
                    append(methodSignature.returnType.typeDesc())
                }

                string to callable
            }
        }.filterNotNull().toMap()
    }

    override fun getFunction(method: Method): FunctionMetadata? {
        return map[signature(method.name, method.parameterTypes, method.returnType)]
                ?.let { FunctionMetadataImpl(it, nameResolver) }
    }

    override fun getConstructor(constructor: Constructor<*>): ConstructorMetadata? {
        return map[signature("<init>", constructor.parameterTypes, Void.TYPE)]
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

internal abstract class CallableMetadataImpl(
        private val proto: ProtoBuf.Callable,
        private val nameResolver: StringTable
) : CallableMetadata {
    override val parameters: List<ParameterMetadata>
        get() = proto.valueParameterList.map { ParameterMetadataImpl(it, nameResolver) }
}

internal class FunctionMetadataImpl(
        proto: ProtoBuf.Callable,
        nameResolver: StringTable
) : CallableMetadataImpl(proto, nameResolver), FunctionMetadata

internal class ConstructorMetadataImpl(
        proto: ProtoBuf.Callable,
        nameResolver: StringTable
) : CallableMetadataImpl(proto, nameResolver), ConstructorMetadata

internal class ParameterMetadataImpl(
        private val proto: ProtoBuf.Callable.ValueParameter,
        private val nameResolver: StringTable
) : ParameterMetadata {
    override val name: String?
        get() = nameResolver.getString(proto.name)

    override val type: TypeMetadata
        get() = TypeMetadataImpl(proto.type, nameResolver)

}

internal class TypeMetadataImpl(
        private val proto: ProtoBuf.Type,
        private val nameResolver: StringTable
) : TypeMetadata {
    override val isNullable: Boolean
        get() = proto.nullable
}


internal object ReflectionLiteImpl {
    private val extensionRegistry = ExtensionRegistryLite.newInstance().apply(JvmProtoBuf::registerAllExtensions)

    fun loadClassMetadata(klass: Class<*>): ClassMetadata? {
        val annotation = klass.declaredAnnotations.singleOrNull { it.annotationType() == KotlinClass::class.java } as KotlinClass? ?: return null
        val bytes = BitEncoding.decodeBytes(annotation.data)
        val input = ByteArrayInputStream(bytes)
        val nameResolver = StringTable.read(input)
        val classProto = ProtoBuf.Class.parseFrom(input, extensionRegistry)
        return ClassMetadataImpl(classProto, nameResolver)
    }
}
