package kotlinx.reflect.lite.tests

import org.junit.*

class KPropertyTest {
    // accessors
    @Test
    fun testMemberPropertyAccessors() = test("properties.accessors.memberPropertyAccessors") { tests.properties.accessors.memberPropertyAccessors.box() }

    @Test
    fun testExtensionPropertyAccessors() = test("properties.accessors.extensionPropertyAccessors") { tests.properties.accessors.extensionPropertyAccessors.box() }

    @Test
    fun testTopLevelPropertyAccessors() = test("properties.accessors.topLevelPropertyAccessors") { tests.properties.accessors.topLevelPropertyAccessors.box() }
}
