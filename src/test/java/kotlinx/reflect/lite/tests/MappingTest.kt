package kotlinx.reflect.lite.tests

import org.junit.*

class MappingTest {
    @Test
    fun testConstructor() = test("mapping.constructor") { tests.mapping.constructor.box() }

    @Test
    fun testFunctions() = test("mapping.functions") { tests.mapping.functions.box() }
}
