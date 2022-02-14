package kotlinx.reflect.lite.name

import kotlinx.metadata.ClassName
import kotlinx.metadata.isLocal

typealias Name = String

data class FqName(val fqName: String) {
    val isRoot: Boolean get() = fqName.isEmpty()

    fun parent(): FqName = fqName.lastIndexOf('.').let { i ->
        if (i >= 0) FqName(fqName.substring(0, i)) else EMPTY
    }

    fun child(name: Name): FqName = FqName("$fqName.$name")

    fun shortName(): Name = fqName.substring(fqName.lastIndexOf('.') + 1)

    fun asString(): String = fqName

    override fun toString(): String = fqName

    companion object {
        @JvmField
        val EMPTY = FqName("")
    }
}

data class ClassId(val packageFqName: FqName, val relativeClassName: FqName, val isLocal: Boolean = false) {
    constructor(packageFqName: FqName, relativeClassName: Name) : this(packageFqName, FqName(relativeClassName))

    constructor(className: ClassName) : this(
        FqName(className.substringBeforeLast('/', "").replace('/', '.')),
        FqName(className.substringAfterLast('/'))
    ) {
        require(!className.isLocal) { TODO("Local class names are not yet supported here: $className") }
    }

    val shortClassName: Name
        get() = relativeClassName.shortName()

    // TODO: investigate if call sites mean to use asJavaLookupFqName instead
    fun asSingleFqName(): FqName =
        if (packageFqName.isRoot) relativeClassName else FqName(packageFqName.asString() + "." + relativeClassName.asString())

    fun asJavaLookupFqName(): String =
        (if (packageFqName.isRoot) "" else (packageFqName.asString() + ".")) +
                relativeClassName.fqName.replace('.', '$')

    fun createNestedClassId(name: Name): ClassId =
        ClassId(packageFqName, relativeClassName.child(name), isLocal)

    fun getOuterClassId(): ClassId? =
        relativeClassName.parent().let {
            if (it.isRoot) null else ClassId(packageFqName, it, isLocal)
        }

    private fun asString(): String {
        return if (packageFqName.isRoot) relativeClassName.asString()
        else packageFqName.asString().replace('.', '/') + "/" + relativeClassName.asString()
    }

    fun asClassName(): ClassName {
        if (isLocal) TODO(asString())
        return asString()
    }

    override fun toString(): String = if (packageFqName.isRoot) "/" + asString() else asString()

    companion object {
        fun topLevel(topLevelFqName: FqName): ClassId =
            ClassId(topLevelFqName.parent(), FqName(topLevelFqName.shortName()))
    }
}

private val SANITIZE_AS_JAVA_INVALID_CHARACTERS = "[^\\p{L}\\p{Digit}]".toRegex()
