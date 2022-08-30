/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.annotations.annotationRetentionAnnotation

import kotlin.test.assertEquals

@Retention(AnnotationRetention.RUNTIME)
annotation class Anno

fun box(): String {
    val a = Anno::class.annotations

    if (a.size != 1) return "Fail 1: $a"
    val ann = a.single() as? Retention ?: return "Fail 2: ${a.single()}"
    assertEquals(AnnotationRetention.RUNTIME, ann.value)

    return "OK"
}
