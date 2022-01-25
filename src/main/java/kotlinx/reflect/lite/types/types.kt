package kotlinx.reflect.lite.types

import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.ClassifierDescriptor

internal class KotlinType(
    val descriptor: ClassifierDescriptor,
    val arguments: List<TypeProjection>,
    val isMarkedNullable: Boolean
) : Annotated {

}

internal class TypeProjection(
    val type: KotlinType
)
