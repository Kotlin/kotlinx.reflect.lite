package org.jetbrains.kotlin.name

data class FqName(private val fqName: String) {
    fun asString(): String = fqName

    val isRoot: Boolean
        get() = fqName.isEmpty()

    override fun toString(): String =
            fqName

    companion object {
        val ROOT = FqName("")
    }
}

data class ClassId(
        val packageFqName: FqName,
        val relativeClassName: FqName,
        val local: Boolean
) {
    fun asString(): String {
        if (packageFqName.isRoot) return relativeClassName.asString()
        return packageFqName.asString().replace('.', '/') + "/" + relativeClassName.asString()
    }

    override fun toString() = asString()
}
