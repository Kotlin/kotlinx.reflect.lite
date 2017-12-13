/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("UNUSED_PARAMETER", "unused")

package kotlinx.reflect.lite.test

import kotlin.reflect.KClass

class Types(param: Int) {
    class Nested {
        fun method(nullableString: String?, nonNullIntArray: IntArray, nullableNested: Nested?): Int = 0
    }

    constructor() : this(0)

    fun notNullListOfStrings(): List<String> = emptyList()
    fun nullableInt(): Int? = null

    fun primitives(z: Boolean, c: Char, b: Byte, s: Short, i: Int, f: Float, j: Long, d: Double) {}
    fun primitiveArrays(z: BooleanArray, c: CharArray, b: ByteArray, s: ShortArray, i: IntArray, f: FloatArray, j: LongArray, d: DoubleArray) {}
    fun mappedCollections(a: Iterable<*>, b: Iterator<*>, c: Collection<*>, d: List<*>, e: Set<*>, f: Map<*, *>, g: Map.Entry<*, *>, h: ListIterator<*>) {}
    fun mappedMutableCollections(
            a: MutableIterable<*>, b: MutableIterator<*>, c: MutableCollection<*>, d: MutableList<*>, e: MutableSet<*>,
            f: MutableMap<*, *>, g: MutableMap.MutableEntry<*, *>, h: MutableListIterator<*>
    ) {}
    fun mappedTypes(a: Any, b: String, c: CharSequence, d: Throwable, e: Cloneable, f: Number, g: Comparable<*>, h: Enum<*>, i: Annotation) {}
    fun functionTypes(a: () -> Unit, b: () -> String, c: (String) -> Unit, d: (String, Int, DoubleArray) -> List<*>,
                      e: (Any, Any, Any?, Array<Any?>, KClass<Any>, Class<Any>, List<Any>, Map<Any, Any>) -> Any?) {}
}

data class DataClass(val field: Int)

class ClassKinds {
    class Class
    interface Interface
    enum class Enum {
        ENTRY { fun foo() {} }
    }
    annotation class Annotation
    object Object
    companion object
}

open class Visibilities {
    constructor()
    protected constructor(i: Int)
    internal constructor(d: Double)
    private constructor(f: Float)

    fun publicFun() {}
    protected fun protectedFun() {}
    internal fun internalFun() {}
    private fun privateFun() {}

    val publicVal = ""
    protected val protectedVal = ""
    internal val internalVal = ""
    private val privateVal = ""
}

class Properties {
    private val backingField: List<String>? = emptyList()

    val delegated: String by lazy { "42" }
}

class EnumerateAllCallables(val property1: String) {
    constructor() : this("")

    fun function1() {}
    fun function2() {}
    val property2 = ""
}

class ExtensionReceiverType {
    fun String.stringExtFun() {}
    fun List<Any>?.nullableListExtFun() {}

    val Int.intExtProp get() = ""
    val Double?.nullableDoubleExtProp get() = ""

    fun nonExtFun() {}
}
