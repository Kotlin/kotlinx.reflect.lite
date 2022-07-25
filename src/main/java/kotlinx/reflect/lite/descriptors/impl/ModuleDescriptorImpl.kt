package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.metadata.internal.common.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.builtins.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.descriptors.ModuleDescriptor
import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.name.*

internal class ModuleDescriptorImpl(internal val classLoader: ClassLoader) : ModuleDescriptor {
    override fun <T> findClass (name: ClassName): ClassDescriptor<T> {
        val fqName = (JavaToKotlinClassMap.mapKotlinToJava(FqName(name.replace('/', '.'))) ?: ClassId(name)).asJavaLookupFqName()
        val jClass = classLoader.tryLoadClass(fqName) ?: error("Failed to load the class: $fqName")
        return ClassDescriptorImpl(jClass as Class<T>)
    }

    // TODO: fix creation of ClassDescriptor
    internal fun Class<*>.getKmClass(): KmClass {
        val className = name
        val builtinClassId = when (className) {
            "[Ljava.lang.Object;" -> ClassId(FqName("kotlin"), "Array")
            // TODO: move this into mapJavaToKotlin
            "[Z" -> ClassId(FqName("kotlin"), "BooleanArray")
            "[B" -> ClassId(FqName("kotlin"), "ByteArray")
            "[C" -> ClassId(FqName("kotlin"), "CharArray")
            "[D" -> ClassId(FqName("kotlin"), "DoubleArray")
            "[F" -> ClassId(FqName("kotlin"), "FloatArray")
            "[I" -> ClassId(FqName("kotlin"), "IntArray")
            "[J" -> ClassId(FqName("kotlin"), "LongArray")
            "[S" -> ClassId(FqName("kotlin"), "ShortArray")
            "java.lang.Void" -> ClassId(FqName("kotlin"), "Nothing") // TODO: ???
            else -> when {
                this.isPrimitive -> {
                    when (this) {
                        Boolean::class.java -> ClassId(FqName("kotlin"), "Boolean")
                        Byte::class.java -> ClassId(FqName("kotlin"), "Byte")
                        Char::class.java -> ClassId(FqName("kotlin"), "Char")
                        Double::class.java -> ClassId(FqName("kotlin"), "Double")
                        Float::class.java -> ClassId(FqName("kotlin"), "Float")
                        Int::class.java -> ClassId(FqName("kotlin"), "Int")
                        Long::class.java -> ClassId(FqName("kotlin"), "Long")
                        Short::class.java -> ClassId(FqName("kotlin"), "Short")
                        else -> error(this)
                    }
                }
                else -> JavaToKotlinClassMap.mapJavaToKotlin(classId.asSingleFqName())
            }
        }
        return if (builtinClassId != null) {
            val packageName = builtinClassId.packageFqName
            // kotlin.collections -> kotlin/collections/collections.kotlin_builtins
            val resourcePath = packageName.asString().replace('.', '/') + '/' + packageName.shortName() + ".kotlin_builtins"
            val bytes = Unit::class.java.classLoader.getResourceAsStream(resourcePath)?.readBytes()
                ?: error("No builtins metadata file found: $resourcePath") // TODO: return null
            val packageFragment = KotlinCommonMetadata.read(bytes)?.toKmModuleFragment()
                ?: error("Incompatible metadata version: $resourcePath") // TODO
            packageFragment.classes.find { it.name == builtinClassId.asClassName() }
                ?: error("Built-in class not found: $builtinClassId in $resourcePath")
        } else {
            val header = getAnnotation(Metadata::class.java)?.let {
                KotlinClassHeader(it.kind, it.metadataVersion, it.data1, it.data2, it.extraString, it.packageName, it.extraInt)
            } ?: error("@Metadata annotation was not found for ${name} ")
            val metadata = KotlinClassMetadata.read(header)
            (metadata as? KotlinClassMetadata.Class)?.toKmClass()
                ?: error("KotlinClassMetadata.Class metadata is only supported for now")
        }
    }
}
