/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.types.jvmErasureOfClass

import kotlin.reflect.jvm.jvmErasure
import kotlin.test.assertEquals

fun string(): String = null!!
fun array(): Array<String> = null!!

fun collection(): Collection<String> = null!!
fun mutableCollection(): MutableCollection<String> = null!!

fun box(): String {
    assertEquals(String::class, ::string.returnType.jvmErasure)
    assertEquals(Array<String>::class, ::array.returnType.jvmErasure)

    assertEquals(Collection::class, ::collection.returnType.jvmErasure)
    assertEquals(MutableCollection::class, ::mutableCollection.returnType.jvmErasure)

    return "OK"
}
