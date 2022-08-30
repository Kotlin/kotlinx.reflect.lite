/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.retentions

import kotlin.test.assertEquals

@Retention(AnnotationRetention.SOURCE)
annotation class SourceAnno

@Retention(AnnotationRetention.BINARY)
annotation class BinaryAnno

@Retention(AnnotationRetention.RUNTIME)
annotation class RuntimeAnno

@SourceAnno
@BinaryAnno
@RuntimeAnno
fun box(): String {
    assertEquals(listOf(RuntimeAnno::class.java), ::box.annotations.map { it.annotationClass.java })
    return "OK"
}
