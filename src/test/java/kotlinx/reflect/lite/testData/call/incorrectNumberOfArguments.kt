package tests.call.incorrectNumberOfArguments

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

var foo: String = ""

class A(private var bar: String = "") {
    fun getBar() = ((A::class.java).kotlinClass as KClass<A>).getMemberByName("bar")
}

object O {
    @JvmStatic
    private var baz: String = ""

    @JvmStatic
    fun getBaz() = (((O::class.java).kotlinClass as KClass<O>).getMemberByName("baz") as KMutableProperty<*>).apply { isAccessible = true }

    fun getGetBaz() = ((O::class.java).kotlinClass as KClass<O>).getMemberByName("getBaz") as KFunction<*>
}

fun check(callable: KCallable<*>, vararg args: Any?) {
    val expected = callable.parameters.size
    val actual = args.size

    if (expected == actual) {
        throw AssertionError("Bad test case: expected and actual number of arguments should differ (was $expected vs $actual)")
    }

    val expectedExceptionMessage = "Callable expects $expected arguments, but $actual were provided."

    try {
        callable.call(*args)
        throw AssertionError("Fail: an IllegalArgumentException should have been thrown")
    } catch (e: IllegalArgumentException) {
        if (e.message != expectedExceptionMessage) {
            // This most probably means that we don't check number of passed arguments in reflection
            // and the default check from Java reflection yields an IllegalArgumentException, but with a not that helpful message
            throw AssertionError("Fail: an exception with an unrecognized message was thrown: \"${e.message}\"" +
                                 "\nExpected message was: $expectedExceptionMessage")
        }
    }
}

private fun testBox() {
    val clazz = Class.forName("tests.call.incorrectNumberOfArguments.IncorrectNumberOfArgumentsKt").kotlinClass
    val box = clazz.getMemberByName("box")
    check(box, null)
    check(box, "")
}

private fun testAConstructor() {
    val aCons = (A::class.java.kotlinClass as KClass<A>).getPrimaryConstructor()
    check(aCons)
    check(aCons, null, "")
}

private fun testGetBaz() {
    val getGetBaz = ((O::class.java).kotlinClass as KClass<O>).getMemberByName("getGetBaz")
    check(getGetBaz)
    check(getGetBaz, null, "")
}

private fun testFoo() {
    val clazz = Class.forName("tests.call.incorrectNumberOfArguments.IncorrectNumberOfArgumentsKt").kotlinClass
    val f = clazz.getMemberByName("foo") as KMutableProperty0<String>
    check(f, null)
    // TODO: args array of size 2 is wrapped into another array of size 1
    // check(f, null, null)
    check(f, arrayOf<Any?>(null))
    check(f, "")

    check(f.getter, null)
    check(f.getter, arrayOf<Any?>(null))
    check(f.getter, "")

    check(f.setter)
    check(f.setter, null, null)
    check(f.setter, null, "")
}

private fun testAccessPrivateBarProperty() {
    val b = A().getBar() as KMutableProperty1<A, String>
    val bar = (A::class.java.kotlinClass as KClass<A>).getMemberByName("bar")
    bar.isAccessible = true
    // TODO: kotlinx.reflect.lite.calls.CallerImpl$FieldGetter cannot access a member of class tests.call.incorrectNumberOfArguments.A with modifiers "private"
    b.call(A())
    check(b)
    check(b, null, null)
    check(b, "", "")

    check(b.getter)
    check(b.getter, null, null)
    check(b.getter, "", "")

    check(b.setter)
    check(b.setter, null)
    check(b.setter, "")

    b.set(A(), "45")
    assertEquals("45", b.get(A()))
}

private fun testAccessPrivateBazProperty() {
    val z = O.getBaz()

    check(z)
    check(z, null, null)
    check(z, "", "")

    check(z.getter)
    check(z.getter, null, null)
    check(z.getter, "", "")

    check(z.setter)
    check(z.setter, null)
    check(z.setter, "")
}

fun box(): String {
    testBox()
    testAConstructor()
    testGetBaz()
    testFoo()
    // testAccessPrivateBarProperty()
    // testAccessPrivateBazProperty()
    return "OK"
}
