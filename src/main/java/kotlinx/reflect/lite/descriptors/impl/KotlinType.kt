package kotlinx.reflect.lite.descriptors.impl

import kotlinx.metadata.*
import kotlinx.reflect.lite.KVariance
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.descriptors.Annotated
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.descriptors.ClassifierDescriptor
import kotlinx.reflect.lite.descriptors.DeclarationDescriptor
import kotlinx.reflect.lite.descriptors.ModuleDescriptor
import kotlinx.reflect.lite.descriptors.TypeParameterDescriptor

internal class KotlinType(
    val descriptor: ClassifierDescriptor,
    val arguments: List<TypeProjection>,
    val isMarkedNullable: Boolean // todo pass annotations
) : Annotated

internal fun KotlinType.isNullableType(): Boolean =
    // TODO: check if this is correct
    isMarkedNullable || (descriptor is TypeParameterDescriptor && descriptor.upperBounds.any { it.isNullableType() })

internal class TypeParameterTable(
    val typeParameters: List<TypeParameterDescriptorImpl>,
    private val parent: TypeParameterTable? = null
) {
    private fun getOrNull(id: Int): TypeParameterDescriptor? =
        typeParameters.find { it.id == id } ?: parent?.getOrNull(id)

    fun get(id: Int): TypeParameterDescriptor =
        getOrNull(id) ?: error("Unknown type parameter with id=$id")

    companion object {
        @JvmField
        val EMPTY = TypeParameterTable(emptyList())
    }
}

internal class TypeProjection(
    val type: KotlinType,
    val isStarProjection: Boolean,
    val projectionKind: KVariance
)

internal fun KmType.toKotlinType(module: ModuleDescriptor, typeParameterTable: TypeParameterTable): KotlinType {
    val classifier = classifier.let { classifier ->
        when (classifier) {
            is KmClassifier.Class -> module.findClass<Any?>(classifier.name)
            is KmClassifier.TypeParameter -> typeParameterTable.get(classifier.id)
            is KmClassifier.TypeAlias -> TODO()
        }
    }
    return KotlinType(
        classifier,
        generateSequence(this, KmType::outerType).flatMap(KmType::arguments).map { (variance, type) ->
            TypeProjection(
                type?.toKotlinType(module, typeParameterTable) ?: TODO("Any type"),
                variance == null,
                variance?.toVariance() ?: KVariance.OUT
            )
        }.toList(),
        Flag.Type.IS_NULLABLE(flags)
    )
}

internal fun List<KmTypeParameter>.toTypeParameters(
    container: DeclarationDescriptor,
    module: ModuleDescriptor,
    parentTable: TypeParameterTable?
): TypeParameterTable {
    val list = ArrayList<TypeParameterDescriptorImpl>(size)
    return TypeParameterTable(list, parentTable).also { table ->
        mapTo(list) { TypeParameterDescriptorImpl(it, module, container, table) }
    }
}

internal val ClassifierDescriptor.kotlinType: KotlinType
    get() = KotlinType(
        this,
        (this as? ClassDescriptor<*>)?.typeParameters?.map {
            TypeProjection(it.kotlinType, false, KVariance.INVARIANT)
        }.orEmpty(),
        false
    )

internal val ClassifierDescriptor.defaultType: KotlinType
    get() = KotlinType(
        this,
        (this as? ClassDescriptor<*>)?.typeParameters?.map {
            TypeProjection(it.defaultType, false, KVariance.INVARIANT)
        }.orEmpty(),
        false
    )

private fun KmVariance.toVariance(): KVariance =
    when (this) {
        KmVariance.INVARIANT -> KVariance.INVARIANT
        KmVariance.IN -> KVariance.IN
        KmVariance.OUT -> KVariance.OUT
    }
