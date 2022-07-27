package tests.createAnnotation.annotationType

import kotlin.test.assertEquals

annotation class Foo

fun box(): String {
    val foo = Foo::class.constructors.single().call()
    assertEquals(Foo::class, foo.annotationClass)
    return "OK"
}