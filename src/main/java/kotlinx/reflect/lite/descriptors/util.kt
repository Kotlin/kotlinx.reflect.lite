package kotlinx.reflect.lite.descriptors

import kotlinx.metadata.*
import kotlinx.reflect.lite.*

internal fun Flags.toVisibility(): KVisibility? =
    when {
        Flag.Common.IS_PRIVATE(this) -> KVisibility.PRIVATE
        Flag.Common.IS_PRIVATE_TO_THIS(this) -> KVisibility.PRIVATE
        Flag.Common.IS_INTERNAL(this) -> KVisibility.INTERNAL
        Flag.Common.IS_PROTECTED(this) -> KVisibility.PROTECTED
        Flag.Common.IS_PUBLIC(this) -> KVisibility.PUBLIC
        Flag.Common.IS_LOCAL(this) -> null
        else -> error("Declaration with unknown visibility")
    }

internal fun KmVariance.toVariance(): KVariance =
    when (this) {
        KmVariance.INVARIANT -> KVariance.INVARIANT
        KmVariance.IN -> KVariance.IN
        KmVariance.OUT -> KVariance.OUT
    }