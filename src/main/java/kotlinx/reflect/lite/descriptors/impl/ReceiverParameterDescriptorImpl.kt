/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.descriptors.impl

import kotlinx.reflect.lite.descriptors.*

internal class ReceiverParameterDescriptorImpl(
    override val type: KotlinType
) : ReceiverParameterDescriptor {
    override val name: String
        get() = "<this>"

    // TODO: receiver parameter descriptor does not need this information
    override val containingDeclaration: CallableDescriptor?
        get() = null
}
