package kotlinx.reflect.lite.descriptors

import kotlinx.metadata.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.descriptors.impl.*
import kotlinx.reflect.lite.descriptors.impl.ClassDescriptorImpl
import kotlinx.reflect.lite.impl.*

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

internal fun CallableDescriptor.isGetterOfUnderlyingPropertyOfInlineClass() =
    this is PropertyGetterDescriptor && property.isUnderlyingPropertyOfInlineClass()

internal fun CallableDescriptor.isUnderlyingPropertyOfInlineClass(): Boolean {
    if (this !is PropertyDescriptor || extensionReceiverParameter != null) return false
    val container = containingClass
    return container is ClassDescriptorImpl && container.kmClass.inlineClassUnderlyingPropertyName == name
}

// TODO: do we need it?
internal fun DeclarationDescriptor?.toInlineClass(): Class<*>? =
    if (this is ClassDescriptor<*> && isValue)
        this.jClass
    else
        null
