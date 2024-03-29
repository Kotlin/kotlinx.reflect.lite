/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

// Partially copied: https://github.com/JetBrains/kotlin/blob/69a13a32691ea3b952f763aa958e276128474718/core/compiler.common.jvm/src/org/jetbrains/kotlin/load/java/JvmAbi.kt
package kotlinx.reflect.lite.builtins

internal object JvmAbi {
    const val INSTANCE_FIELD = "INSTANCE"
    const val DEFAULT_PARAMS_IMPL_SUFFIX = "\$default"
    const val DEFAULT_IMPLS_SUFFIX = "\$DefaultImpls"
    const val IMPL_SUFFIX_FOR_INLINE_CLASS_MEMBERS = "-impl"
}
