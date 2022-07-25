package kotlinx.reflect.lite.impl

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.*

internal class KPackageImpl<T : Any?>(
    override val descriptor: PackageDescriptor<T>
) : KPackage<T>, KDeclarationContainerImpl()
