package kotlinx.reflect.lite.builtins

import kotlinx.reflect.lite.name.*

internal enum class PrimitiveType(val typeName: Name) {
    BOOLEAN("Boolean"), CHAR("Char"), BYTE("Byte"), SHORT("Short"), INT("Int"), FLOAT("Float"), LONG("Long"), DOUBLE("Double");

    val arrayTypeName: Name = typeName + "Array"

    val typeFqName: FqName = StandardNames.BUILT_INS_PACKAGE_FQ_NAME.child(typeName)
    val arrayTypeFqName: FqName = StandardNames.BUILT_INS_PACKAGE_FQ_NAME.child(arrayTypeName)

    companion object {
        val NUMBER_TYPES = setOf(CHAR, BYTE, SHORT, INT, FLOAT, LONG, DOUBLE)
    }
}
