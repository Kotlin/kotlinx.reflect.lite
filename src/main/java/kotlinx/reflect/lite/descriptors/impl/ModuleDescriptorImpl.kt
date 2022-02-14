package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.builtins.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.descriptors.ModuleDescriptor
import kotlinx.reflect.lite.impl.KClassImpl
import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.name.*

internal class ModuleDescriptorImpl(private val classLoader: ClassLoader) : ModuleDescriptor {
    override fun findClass(name: ClassName): ClassDescriptor {
        val fqName = (JavaToKotlinClassMap.mapKotlinToJava(FqName(name.replace('/', '.'))) ?: ClassId(name)).asJavaLookupFqName()
        val jClass = classLoader.tryLoadClass(fqName) ?: error("Failed to load the class: $fqName")
        return KClassImpl(jClass).descriptor
    }
}