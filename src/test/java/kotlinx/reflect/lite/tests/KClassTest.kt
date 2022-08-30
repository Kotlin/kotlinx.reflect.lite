/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

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
    fun testClassesWithoutConstructors() = test("constructors.classesWithoutConstructors") { tests.constructors.classesWithoutConstructors.box() }

    @Test
    fun testConstructorName() = test("constructors.constructorName") { tests.constructors.constructorName.box() }

    @Test
    fun testSimpleGetConstructors() = test("constructors.simpleGetConstructors") { tests.constructors.simpleGetConstructors.box() }

    @Test
    fun testPrimaryConstructor() = test("constructors.primaryConstructor") { tests.constructors.primaryConstructor.box() }

    // members
    @Test
    fun testClassMembers() = test("classes.classMembers") { tests.classes.classMembers.box() }
}
