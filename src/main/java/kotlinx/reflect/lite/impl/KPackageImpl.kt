/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

internal class KPackageImpl<T : Any?>(
    override val descriptor: PackageDescriptor<T>
) : KPackage<T>, KDeclarationContainerImpl()
