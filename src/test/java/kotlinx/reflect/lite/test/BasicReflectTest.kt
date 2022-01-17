package kotlinx.reflect.lite.test

import kotlinx.reflect.lite.*
import org.junit.Test
import kotlin.test.*

class BasicReflectTest {
    @Test
    fun testConstructor() {
        val kClass = ReflectionLite.loadClassMetadata(Types::class.java)!!
        assertEquals(2, kClass.constructors.size)
        val primaryConstructor = kClass.constructors.single { it.parameters.size == 1 }
        assertTrue(primaryConstructor.name == "<init>")

        val parameter = primaryConstructor.parameters.single()
        assertTrue(parameter.name == "param")
    }

    @Test
    fun testDataClass() {
        assertTrue(ReflectionLite.loadClassMetadata(DataClass::class.java)!!.isData)
        assertFalse(ReflectionLite.loadClassMetadata(Types::class.java)!!.isData)
    }

    @Test
    fun testClassKinds() {
        assertTrue(ReflectionLite.loadClassMetadata(ClassKinds.Enum::class.java)!!.isFinal)
        assertTrue(ReflectionLite.loadClassMetadata(ClassKinds.Companion::class.java)!!.isCompanion)
    }
}
