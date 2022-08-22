package kotlinx.reflect.lite.unusedTestData.constructors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

enum class TestEnum(val id: String? = null) {
    ENUM1(id = "enum1_id"),

    ENUM2(id = "enum2_id") {
        override fun test() {
            ENUM1.test()
        }
    };

    open fun test() {
    }
}

fun box(): String {
    assertEquals(listOf("fun <init>(kotlin.String?): tests.constructors.enumEntry.TestEnum"), TestEnum.ENUM1::class.java.kotlin.constructors.map { it.toString() })
    assertEquals(listOf(), (TestEnum.ENUM2::class.java.kotlin).constructors.map { it.toString() })

    return "OK"
}
