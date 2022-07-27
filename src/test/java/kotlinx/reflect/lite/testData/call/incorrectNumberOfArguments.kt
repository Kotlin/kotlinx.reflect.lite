package tests.call.incorrectNumberOfArguments

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.tests.*

var foo: String = ""

class A(private var bar: String = "") {
    fun getBar() = (A::class.java).toLiteKClass().getMemberByName("bar")
}

object O {
    @JvmStatic
    private var baz: String = ""

    @JvmStatic // TODO: support isAccessible
    //fun getBaz() = (O::class.members.single { it.name == "baz" } as KMutableProperty<*>).apply { isAccessible = true }
    fun getBaz() = (O::class.java).toLiteKClass().getMemberByName("baz") as KMutableProperty<*>

    fun getGetBaz() = (O::class.java).toLiteKClass().getMemberByName("getBaz") as KFunction<*>
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

fun box(): String {
    val clazz = Class.forName("tests.call.incorrectNumberOfArguments.IncorrectNumberOfArgumentsKt").toLiteKDeclarationContainer()
    val box = clazz.getMemberByName("box")
    check(box, null)
    check(box, "")

    val aCons = A::class.java.toLiteKClass().getPrimaryConstructor()
    check(aCons)
    check(aCons, null, "")

    val getGetBaz = (O::class.java).toLiteKClass().getMemberByName("getGetBaz")
    check(getGetBaz)
    check(getGetBaz, null, "")

    val f = clazz.getMemberByName("foo") as KMutableProperty0<String>
    check(f, null)
    // TODO: args array of size 2 is wrapped into another array of size 1
//    check(f, null, null)
    check(f, arrayOf<Any?>(null))
    check(f, "")

    check(f.getter, null)
    check(f.getter, arrayOf<Any?>(null))
    check(f.getter, "")

    check(f.setter)
    check(f.setter, null, null)
    check(f.setter, null, "")

    // TODO: KCallable<*>.isAccessible
    // val b = A().getBar()
    // check(b)
//    check(b, null, null)
//    check(b, "", "")
//
//    check(b.getter)
//    check(b.getter, null, null)
//    check(b.getter, "", "")
//
//    check(b.setter)
//    check(b.setter, null)
//    check(b.setter, "")
//

//    val z = O.getBaz()
//
//    check(z)
//    check(z, null, null)
//    check(z, "", "")
//
//    check(z.getter)
//    check(z.getter, null, null)
//    check(z.getter, "", "")
//
//    check(z.setter)
//    check(z.setter, null)
//    check(z.setter, "")

    return "OK"
}
