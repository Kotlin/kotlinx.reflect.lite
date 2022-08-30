// Partially copied from: https://github.com/JetBrains/kotlin/blob/b573532d8cbf9bf5347adb40d0774c21c2d35dc0/core/compiler.common/src/org/jetbrains/kotlin/builtins/StandardNames.kt
package kotlinx.reflect.lite.builtins

import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.name.*

internal object StandardNames {
    val BUILT_INS_PACKAGE_FQ_NAME = FqName("kotlin")

    object FqNames {
        @JvmField val array = FqName("kotlin.Array")
        @JvmField val any = FqName("kotlin.Any")
        @JvmField val string = FqName("kotlin.String")
        @JvmField val charSequence = FqName("kotlin.CharSequence")
        @JvmField val throwable = FqName("kotlin.Throwable")
        @JvmField val cloneable = FqName("kotlin.Cloneable")
        @JvmField val number = FqName("kotlin.Number")
        @JvmField val comparable = FqName("kotlin.Comparable")
        @JvmField val _enum = FqName("kotlin.Enum")
        @JvmField val annotation = FqName("kotlin.Annotation")
        @JvmField val nothing = FqName("kotlin.Nothing")
        @JvmField val _boolean = FqName("kotlin.Boolean")

        @JvmField val iterable = FqName("kotlin.collections.Iterable")
        @JvmField val iterator = FqName("kotlin.collections.Iterator")
        @JvmField val collection = FqName("kotlin.collections.Collection")
        @JvmField val list = FqName("kotlin.collections.List")
        @JvmField val set = FqName("kotlin.collections.Set")
        @JvmField val listIterator = FqName("kotlin.collections.ListIterator")
        @JvmField val map = FqName("kotlin.collections.Map")
        @JvmField val mapEntry = FqName("kotlin.collections.Map.Entry")
        @JvmField val mutableIterable = FqName("kotlin.collections.MutableIterable")
        @JvmField val mutableIterator = FqName("kotlin.collections.MutableIterator")
        @JvmField val mutableCollection = FqName("kotlin.collections.MutableCollection")
        @JvmField val mutableList = FqName("kotlin.collections.MutableList")
        @JvmField val mutableSet = FqName("kotlin.collections.MutableSet")
        @JvmField val mutableListIterator = FqName("kotlin.collections.MutableListIterator")
        @JvmField val mutableMap = FqName("kotlin.collections.MutableMap")
        @JvmField val mutableMapEntry = FqName("kotlin.collections.MutableMap.Entry")
    }
}

internal object KotlinBuiltInsImpl {
    val anyType: KotlinType = KotlinType((Any::class.java.kDeclarationContainer as KClassImpl<*>).descriptor, emptyList(), false)
    val anyNType: KotlinType = KotlinType((Any::class.java.kDeclarationContainer as KClassImpl<*>).descriptor, emptyList(), true)
}
