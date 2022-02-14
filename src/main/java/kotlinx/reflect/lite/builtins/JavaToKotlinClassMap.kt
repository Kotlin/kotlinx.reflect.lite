package kotlinx.reflect.lite.builtins

import kotlinx.reflect.lite.builtins.StandardNames.FQ_NAMES
import kotlinx.reflect.lite.misc.*
import kotlinx.reflect.lite.name.*

object JavaToKotlinClassMap {
    private const val BIG_FUNCTION_ARITY = 23

    private const val NUMBERED_FUNCTION_PREFIX = "kotlin.Function"
    private const val NUMBERED_K_FUNCTION_PREFIX = "kotlin.reflect.KFunction"
    private const val NUMBERED_SUSPEND_FUNCTION_PREFIX = "kotlin.coroutines.SuspendFunction"
    private const val NUMBERED_K_SUSPEND_FUNCTION_PREFIX = "kotlin.reflect.KSuspendFunction"

    private val FUNCTION_N_CLASS_ID = ClassId.topLevel(FqName("kotlin.jvm.functions.FunctionN"))
    private val K_FUNCTION_CLASS_ID = ClassId.topLevel(FqName("kotlin.reflect.KFunction"))

    private val javaToKotlin = hashMapOf<FqName, ClassId>()
    private val kotlinToJava = hashMapOf<FqName, ClassId>()

    data class PlatformMutabilityMapping(
        val javaClass: ClassId,
        val kotlinReadOnly: ClassId,
        val kotlinMutable: ClassId
    )

    private inline fun <reified T> mutabilityMapping(kotlinReadOnly: ClassId, kotlinMutable: FqName): PlatformMutabilityMapping {
        val mutableClassId = ClassId(
            kotlinReadOnly.packageFqName,
            FqName(kotlinMutable.asString().substring(kotlinReadOnly.packageFqName.asString().length + 1)),
            false
        )
        return PlatformMutabilityMapping(T::class.java.classId, kotlinReadOnly, mutableClassId)
    }

    private val mutabilityMappings = listOf(
        mutabilityMapping<Iterable<*>>(ClassId.topLevel(FQ_NAMES.iterable), FQ_NAMES.mutableIterable),
        mutabilityMapping<Iterator<*>>(ClassId.topLevel(FQ_NAMES.iterator), FQ_NAMES.mutableIterator),
        mutabilityMapping<Collection<*>>(ClassId.topLevel(FQ_NAMES.collection), FQ_NAMES.mutableCollection),
        mutabilityMapping<List<*>>(ClassId.topLevel(FQ_NAMES.list), FQ_NAMES.mutableList),
        mutabilityMapping<Set<*>>(ClassId.topLevel(FQ_NAMES.set), FQ_NAMES.mutableSet),
        mutabilityMapping<ListIterator<*>>(ClassId.topLevel(FQ_NAMES.listIterator), FQ_NAMES.mutableListIterator),
        mutabilityMapping<Map<*, *>>(ClassId.topLevel(FQ_NAMES.map), FQ_NAMES.mutableMap),
        mutabilityMapping<Map.Entry<*, *>>(
            ClassId.topLevel(FQ_NAMES.map).createNestedClassId(FQ_NAMES.mapEntry.shortName()), FQ_NAMES.mutableMapEntry
        )
    )

    init {
        addTopLevel(Any::class.java, FQ_NAMES.any)
        addTopLevel(String::class.java, FQ_NAMES.string)
        addTopLevel(CharSequence::class.java, FQ_NAMES.charSequence)
        addTopLevel(Throwable::class.java, FQ_NAMES.throwable)
        addTopLevel(Cloneable::class.java, FQ_NAMES.cloneable)
        addTopLevel(Number::class.java, FQ_NAMES.number)
        addTopLevel(Comparable::class.java, FQ_NAMES.comparable)
        addTopLevel(Enum::class.java, FQ_NAMES._enum)
        addTopLevel(Annotation::class.java, FQ_NAMES.annotation)

        for ((javaClassId, readOnlyClassId, mutableClassId) in mutabilityMappings) {
            add(javaClassId, readOnlyClassId)
            addKotlinToJava(mutableClassId.asSingleFqName(), javaClassId)
        }

        for (jvmType in JvmPrimitiveType.values()) {
            add(
                ClassId.topLevel(jvmType.wrapperFqName),
                ClassId.topLevel(jvmType.primitiveType.typeFqName)
            )
        }

        for (classId in CompanionObjectMapping.allClassesWithIntrinsicCompanions()) {
            add(
                ClassId.topLevel(FqName("kotlin.jvm.internal." + classId.shortClassName + "CompanionObject")),
                classId.createNestedClassId("Companion")
            )
        }

        for (i in 0 until BIG_FUNCTION_ARITY) {
            add(ClassId.topLevel(FqName("kotlin.jvm.functions.Function$i")), ClassId.topLevel(FqName("kotlin.Function$i")))
            addKotlinToJava(FqName(NUMBERED_K_FUNCTION_PREFIX + i), K_FUNCTION_CLASS_ID)
        }
        for (i in 0 until BIG_FUNCTION_ARITY - 1) {
            addKotlinToJava(FqName("kotlin.reflect.KSuspendFunction$i"), K_FUNCTION_CLASS_ID)
        }

        addKotlinToJava(FQ_NAMES.nothing, Void::class.java.classId)
    }

    /**
     * E.g.
     * java.lang.String -> kotlin.String
     * java.lang.Integer -> kotlin.Int
     * kotlin.jvm.internal.IntCompanionObject -> kotlin.Int.Companion
     * java.util.List -> kotlin.List
     * java.util.Map.Entry -> kotlin.Map.Entry
     * java.lang.Void -> null
     * kotlin.jvm.functions.Function3 -> kotlin.Function3
     * kotlin.jvm.functions.FunctionN -> null // Without a type annotation like @Arity(n), it's impossible to find out arity
     */
    fun mapJavaToKotlin(fqName: FqName): ClassId? {
        return javaToKotlin[fqName]
    }

    /**
     * E.g.
     * kotlin.Throwable -> java.lang.Throwable
     * kotlin.Int -> java.lang.Integer
     * kotlin.Int.Companion -> kotlin.jvm.internal.IntCompanionObject
     * kotlin.Nothing -> java.lang.Void
     * kotlin.IntArray -> null
     * kotlin.Function3 -> kotlin.jvm.functions.Function3
     * kotlin.coroutines.SuspendFunction3 -> kotlin.jvm.functions.Function4
     * kotlin.Function42 -> kotlin.jvm.functions.FunctionN
     * kotlin.coroutines.SuspendFunction42 -> kotlin.jvm.functions.FunctionN
     * kotlin.reflect.KFunction3 -> kotlin.reflect.KFunction
     * kotlin.reflect.KSuspendFunction3 -> kotlin.reflect.KFunction
     * kotlin.reflect.KFunction42 -> kotlin.reflect.KFunction
     * kotlin.reflect.KSuspendFunction42 -> kotlin.reflect.KFunction
     */
    fun mapKotlinToJava(kotlinFqName: FqName): ClassId? = when {
        isKotlinFunctionWithBigArity(kotlinFqName, NUMBERED_FUNCTION_PREFIX) -> FUNCTION_N_CLASS_ID
        isKotlinFunctionWithBigArity(kotlinFqName, NUMBERED_SUSPEND_FUNCTION_PREFIX) -> FUNCTION_N_CLASS_ID
        isKotlinFunctionWithBigArity(kotlinFqName, NUMBERED_K_FUNCTION_PREFIX) -> K_FUNCTION_CLASS_ID
        isKotlinFunctionWithBigArity(kotlinFqName, NUMBERED_K_SUSPEND_FUNCTION_PREFIX) -> K_FUNCTION_CLASS_ID
        else -> kotlinToJava[kotlinFqName]
    }

    private fun isKotlinFunctionWithBigArity(kotlinFqName: FqName, prefix: String): Boolean {
        val arityString = kotlinFqName.asString().substringAfter(prefix, "")
        if (arityString.isNotEmpty() && !arityString.startsWith('0')) {
            val arity = arityString.toIntOrNull()
            return arity != null && arity >= BIG_FUNCTION_ARITY
        }
        return false
    }

    private fun add(javaClassId: ClassId, kotlinClassId: ClassId) {
        javaToKotlin[javaClassId.asSingleFqName()] = kotlinClassId
        addKotlinToJava(kotlinClassId.asSingleFqName(), javaClassId)
    }

    private fun addTopLevel(javaClass: Class<*>, kotlinFqName: FqName) {
        add(javaClass.classId, ClassId.topLevel(kotlinFqName))
    }

    private fun addKotlinToJava(kotlinFqName: FqName, javaClassId: ClassId) {
        kotlinToJava[kotlinFqName] = javaClassId
    }
}
