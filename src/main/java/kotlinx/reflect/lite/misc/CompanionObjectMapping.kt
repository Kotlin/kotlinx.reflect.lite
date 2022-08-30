/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.misc

import kotlinx.reflect.lite.builtins.*
import kotlinx.reflect.lite.builtins.StandardNames.FqNames
import kotlinx.reflect.lite.name.*

internal object CompanionObjectMapping {
    private val classIds: Set<ClassId> =
        (PrimitiveType.NUMBER_TYPES.map(PrimitiveType::typeFqName) +
                FqNames.string +
                FqNames._boolean +
                FqNames._enum).mapTo(linkedSetOf(), (ClassId)::topLevel)

    fun allClassesWithIntrinsicCompanions(): Set<ClassId> = classIds
}
