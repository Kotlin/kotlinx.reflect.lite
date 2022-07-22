package kotlinx.reflect.lite.tests

import org.junit.*

class KPropertyTest {
    // accessors
    @Test
    fun testMemberPropertyAccessors() = test("properties.accessors.memberPropertyAccessors.box()") { tests.properties.accessors.memberPropertyAccessors.box() }


}
