package kotlinx.reflect.lite.misc

import kotlinx.reflect.lite.builtins.*
import kotlinx.reflect.lite.builtins.StandardNames.FQ_NAMES
import kotlinx.reflect.lite.descriptors.ClassDescriptor
import kotlinx.reflect.lite.name.*
import java.util.*

internal object CompanionObjectMapping {
    private val classIds: Set<ClassId> =
        (PrimitiveType.NUMBER_TYPES.map(PrimitiveType::typeFqName) +
                FQ_NAMES.string +
                FQ_NAMES._boolean +
                FQ_NAMES._enum).mapTo(linkedSetOf(), (ClassId)::topLevel)

    fun allClassesWithIntrinsicCompanions(): Set<ClassId> =
        Collections.unmodifiableSet(classIds)

//    fun isMappedIntrinsicCompanionObject(classDescriptor: ClassDescriptor): Boolean =
//        classDescriptor.isCompanionObject && classDescriptor.classId.getOuterClassId() in classIds
}
