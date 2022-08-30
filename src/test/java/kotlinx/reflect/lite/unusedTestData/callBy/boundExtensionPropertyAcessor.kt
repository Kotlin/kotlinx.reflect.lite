/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.boundExtensionPropertyAcessor

import kotlin.test.assertEquals

val String.plusK: String
    get() = this + "K"

fun box(): String =
        ("O"::plusK).getter.callBy(mapOf())
