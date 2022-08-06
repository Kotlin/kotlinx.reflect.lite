package kotlinx.reflect.lite.tests

import org.junit.*

class MappingTest {
    @Test
    fun testConstructor() = test("mapping.constructor") { tests.mapping.constructor.box() }

    @Test
    fun testFunctions() = test("mapping.functions") { tests.mapping.functions.box() }

    @Test
    fun testExtensionProperty() = test("mapping.extensionProperty") { tests.mapping.extensionProperty.box() }

    @Test
    fun testTopLevelProperty() = test("mapping.topLevelProperty") { tests.mapping.topLevelProperty.box() }

    @Test
    fun testInterfaceCompanionPropertyWithJvmField() = test("mapping.interfaceCompanionPropertyWithJvmField") { tests.mapping.interfaceCompanionPropertyWithJvmField.box() }

    // javaType
    @Test
    fun testTypesConstructors() = test("mapping.mapping.types.constructors") { tests.mapping.types.constructors.box() }

    @Test
    fun testTypesTopLevelFunctions() = test("mapping.mapping.types.topLevelFunctions") { tests.mapping.types.topLevelFunctions.box() }

    @Test
    fun testTypesMemberFunctions() = test("mapping.mapping.types.memberFunctions") { tests.mapping.types.memberFunctions.box() }

    @Test
    fun testTypesPropertyAccessors() = test("mapping.mapping.types.propertyAccessors") { tests.mapping.types.propertyAccessors.box() }

    @Test
    fun testTypesArray() = test("mapping.mapping.types.array") { tests.mapping.types.array.box() }

    @Test
    fun testTypesGenericArrayElementType() = test("mapping.mapping.types.genericArrayElementType") { tests.mapping.types.genericArrayElementType.box() }

    @Test
    fun testTypesTypeParameters() = test("mapping.mapping.types.typeParameters") { tests.mapping.types.typeParameters.box() }

    @Test
    fun testTypesParameterizedTypeArgument() = test("mapping.mapping.types.parameterizedTypeArgument") { tests.mapping.types.parameterizedTypeArgument.box() }

    @Test
    fun testTypesParameterizedTypes() = test("mapping.mapping.types.parameterizedTypes") { tests.mapping.types.parameterizedTypes.box() }

    @Test
    fun testTypesSupertypes() = test("mapping.mapping.types.supertypes") { tests.mapping.types.supertypes.box() }

    @Test
    fun testTypesUnit() = test("mapping.mapping.types.unit") { tests.mapping.types.unit.box() }
}
