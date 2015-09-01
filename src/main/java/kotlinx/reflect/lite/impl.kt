package kotlinx.reflect.lite

import com.google.protobuf.ExtensionRegistryLite
import org.jetbrains.kotlin.serialization.ProtoBuf
import org.jetbrains.kotlin.serialization.jvm.BitEncoding
import org.jetbrains.kotlin.serialization.jvm.JvmProtoBuf
import org.jetbrains.kotlin.serialization.jvm.JvmProtoBuf.registerAllExtensions
import java.io.ByteArrayInputStream
import java.lang.reflect.Method
import kotlin.jvm.internal.KotlinClass
import java.lang.reflect.Array as ReflectArray

internal class ClassMetadataImpl(
        private val proto: ProtoBuf.Class,
        private val nameResolver: StringTable
) : ClassMetadata {
    private val map: Map<String, ProtoBuf.Callable> by lazySoft {
        proto.memberList.map { callable ->
            callable.getExtension(JvmProtoBuf.methodSignature)?.let { methodSignature ->
                fun JvmProtoBuf.JvmType.typeDesc(): String {
                    return "[".repeat(arrayDimension) +
                            if (hasPrimitiveType())
                                "VZCBSIFJD"[primitiveType.ordinal()]
                            else
                                "L" + nameResolver.getInternalName(classFqName) + ";"
                }

                val string = StringBuilder {
                    append(nameResolver.getString(methodSignature.name))
                    methodSignature.parameterTypeList.joinTo(this, separator = "", prefix = "(", postfix = ")", transform = JvmProtoBuf.JvmType::typeDesc)
                    append(methodSignature.returnType.typeDesc())
                }.toString()

                string to callable
            }
        }.filterNotNull().toMap()
    }

    override fun getFunction(method: Method): FunctionMetadata? {
        return map[method.signature()]?.let { FunctionMetadataImpl(it, nameResolver) }
    }

    private fun Method.signature(): String {
        fun Class<*>.desc(): String {
            if (this == Void.TYPE) return "V"
            return ReflectArray.newInstance(this, 0).javaClass.name.substring(1).replace('.', '/')
        }

        return StringBuilder {
            append(name)
            parameterTypes.joinTo(this, separator = "", prefix = "(", postfix = ")", transform = Class<*>::desc)
            append(returnType.desc())
        }.toString()
    }
}

internal class FunctionMetadataImpl(
        private val proto: ProtoBuf.Callable,
        private val nameResolver: StringTable
) : FunctionMetadata {
    override val parameters: List<ParameterMetadata>
        get() = proto.valueParameterList.map { ParameterMetadataImpl(it, nameResolver) }

}

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
