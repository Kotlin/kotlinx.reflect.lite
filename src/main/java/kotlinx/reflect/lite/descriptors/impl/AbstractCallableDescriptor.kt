/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.CallableDescriptor

internal interface AbstractCallableDescriptor : CallableDescriptor {
    val flags: Flags

    override val visibility: KVisibility? // todo add DescriptorVisibility maybe for consistency
        get() = flags.toVisibility()

    val typeParameterTable: TypeParameterTable

    override val isFinal: Boolean
        get() = Flag.Common.IS_FINAL(flags)
    override val isOpen: Boolean
        get() = Flag.Common.IS_OPEN(flags)
    override val isAbstract: Boolean
        get() = Flag.Common.IS_ABSTRACT(flags)
}
