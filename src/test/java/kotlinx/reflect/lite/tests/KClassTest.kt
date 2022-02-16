package kotlinx.reflect.lite.tests

import org.junit.*

class KClassTest {
    // names
    @Test
    fun testClassSimpleName() = test("classes.classSimpleName") { tests.classes.classSimpleName.box() }

    @Test
    fun testLocalClassSimpleName() = test("classes.localClassSimpleName") { tests.classes.localClassSimpleName.box() }

    @Test
    fun testQualifiedName() = test("classes.qualifiedName") { tests.classes.qualifiedName.box() }

    @Ignore
    @Test
    fun testQualifiedNameOfStandardClasses() = test("classes.qualifiedNameOfStandardClasses") { tests.classes.qualifiedNameOfStandardClasses.box() }

    // nested classes
    @Test
    fun testNestedClasses() = test("classes.nestedClasses") { tests.classes.nestedClasses.box() }

    // sealed classes
    @Test
    fun testSealedSubClasses() = test("classes.sealedSubclasses") { tests.classes.sealedSubclasses.box() }

    // constructors
    @Test
    fun testClassesWithoutConstructors() = test("classes.classesWithoutConstructors") { tests.constructors.classesWithoutConstructors.box() }

    @Test
    fun testConstructorName() = test("classes.constructorName") { tests.constructors.constructorName.box() }

    @Ignore
    @Test
    fun testEnumEntry() = test("classes.enumEntry") { tests.constructors.enumEntry.box() }

    @Test
    fun testSimpleGetConstructors() = test("classes.simpleGetConstructors") { tests.constructors.simpleGetConstructors.box() }

    // members
    @Test
    fun testClassMembers() = test("classes.classMembers") { tests.classes.classMembers.box() }
}