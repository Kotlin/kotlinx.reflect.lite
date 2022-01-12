package kotlinx.reflect.lite.test

import kotlinx.reflect.lite.*
import org.junit.Test
import kotlin.test.*

class BasicReflectTest {

    @Test
    fun testReturnTypeAndNullability() {
        val kClass = ReflectionLite.loadClassMetadata(Types::class.java)!!
        val notNullListOfStrings = kClass.functions.single { it.name == "notNullListOfStrings" }
        assertTrue(notNullListOfStrings.parameters.isEmpty())
    }

    @Test
    fun testParameterNamesAndNullability() {
        val kClass = ReflectionLite.loadClassMetadata(Types.Nested::class.java)!!
        val method = kClass.functions.single { it.name == "method" }

        val parameters = method.parameters
        assertEquals(listOf("nullableString", "nonNullIntArray", "nullableNested"), parameters.map { it.name })
        assertEquals(listOf(true, false, true), parameters.map { it.type?.isMarkedNullable })
    }

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

    @Test
    fun testProperties() {
        val kClass = ReflectionLite.loadClassMetadata(Properties::class.java)!!
        val backingField = kClass.properties.single { it.name == "backingField" }

        assertEquals("backingField", backingField.name)
    }

    @Test
    fun testCallableModifiers() {
        val kClass = ReflectionLite.loadClassMetadata(CallableModifiers::class.java)!!

        assertTrue(kClass.functions.single { it.name == "inline" }.isInline)
        assertTrue(kClass.functions.single { it.name == "external" }.isExternal)
        assertTrue(kClass.functions.single { it.name == "plus" }.isOperator)
        assertTrue(kClass.functions.single { it.name == "infix" }.isInfix)

        assertTrue(kClass.properties.single { it.name == "lateinit" }.isLateinit)
        assertTrue(kClass.properties.single { it.name == "const" }.isConst)

        assertFalse(kClass.functions.single { it.name == "external" }.isInline)
        assertFalse(kClass.functions.single { it.name == "plus" }.isExternal)
        assertFalse(kClass.functions.single { it.name == "infix" }.isOperator)
        assertFalse(kClass.functions.single { it.name == "suspend" }.isInfix)
        assertFalse(kClass.functions.single { it.name == "inline" }.isSuspend)

        assertFalse(kClass.properties.single { it.name == "lateinit" }.isConst)
        assertFalse(kClass.properties.single { it.name == "const" }.isLateinit)
    }

    @Test
    fun testParameterVararg() {
        val kClass = ReflectionLite.loadClassMetadata(ParameterVararg::class.java)!!

        val parameters = kClass.functions.single { it.name == "foo" }.parameters
        assertFalse(parameters[0].type!!.isMarkedNullable)

        assertTrue(parameters[1].isVararg)
        assertFalse(parameters[1].type!!.isMarkedNullable)
    }
}
