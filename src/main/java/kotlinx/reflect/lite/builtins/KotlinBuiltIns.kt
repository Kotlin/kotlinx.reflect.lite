package kotlinx.reflect.lite.builtins

import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.impl.KClassImpl
import kotlinx.reflect.lite.name.*
import kotlinx.reflect.lite.types.*
import kotlinx.reflect.lite.types.KotlinType

internal object StandardNames {
    val BUILT_INS_PACKAGE_FQ_NAME = FqName("kotlin")

    val FQ_NAMES = FqNames()
    class FqNames {
        val array = FqName("kotlin.Array")
        val any = FqName("kotlin.Any")
        val string = FqName("kotlin.String")
        val charSequence = FqName("kotlin.CharSequence")
        val throwable = FqName("kotlin.Throwable")
        val cloneable = FqName("kotlin.Cloneable")
        val number = FqName("kotlin.Number")
        val comparable = FqName("kotlin.Comparable")
        val _enum = FqName("kotlin.Enum")
        val annotation = FqName("kotlin.Annotation")
        val nothing = FqName("kotlin.Nothing")
        val _boolean = FqName("kotlin.Boolean")

        val iterable = FqName("kotlin.collections.Iterable")
        val iterator = FqName("kotlin.collections.Iterator")
        val collection = FqName("kotlin.collections.Collection")
        val list = FqName("kotlin.collections.List")
        val set = FqName("kotlin.collections.Set")
        val listIterator = FqName("kotlin.collections.ListIterator")
        val map = FqName("kotlin.collections.Map")
        val mapEntry = FqName("kotlin.collections.Map.Entry")
        val mutableIterable = FqName("kotlin.collections.MutableIterable")
        val mutableIterator = FqName("kotlin.collections.MutableIterator")
        val mutableCollection = FqName("kotlin.collections.MutableCollection")
        val mutableList = FqName("kotlin.collections.MutableList")
        val mutableSet = FqName("kotlin.collections.MutableSet")
        val mutableListIterator = FqName("kotlin.collections.MutableListIterator")
        val mutableMap = FqName("kotlin.collections.MutableMap")
        val mutableMapEntry = FqName("kotlin.collections.MutableMap.Entry")
    }

    val CLASS_IDS = ClassIds()
    class ClassIds {
        val any = ClassId(BUILT_INS_PACKAGE_FQ_NAME, "Any")
    }
}