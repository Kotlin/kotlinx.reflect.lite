

package tests.call.equalsHashCodeToString

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.tests.*

class A

data class D(val s: String)

fun box(): String {
    val aEquals = (A::class.java).toLiteKClass().getMemberByName("equals") as KFunction<Boolean>
    val aHashCode = (A::class.java).toLiteKClass().getMemberByName("hashCode") as KFunction<Int>
    val aToString = (A::class.java).toLiteKClass().getMemberByName("toString") as KFunction<String>
    val a = A()
    assert(aEquals.call(a, a))
    assert(!aEquals.call(a, 0))
    assert(aHashCode.call(a) == aHashCode.call(a))
    assert(aHashCode.call(a) == A::hashCode.call(a))
    assert(aToString.call(a).startsWith("tests.call.equalsHashCodeToString.A@"))

    val dEquals = (A::class.java).toLiteKClass().getMemberByName("equals") as KFunction<Boolean>
    val dHashCode = (A::class.java).toLiteKClass().getMemberByName("hashCode") as KFunction<Int>
    val dToString = (A::class.java).toLiteKClass().getMemberByName("toString") as KFunction<String>

    assert(dEquals.call(D("foo"), D("foo")))
    assert(!dEquals.call(D("foo"), D("bar")))
    assert(dHashCode.call(D("foo")) == D::hashCode.call(D("foo")))
    assert(dToString.call(D("foo")) == "D(s=foo)")

    val intEquals = (Int::class.java).toLiteKClass().getMemberByName("equals") as KFunction<Boolean>
    val intHashCode = (Int::class.java).toLiteKClass().getMemberByName("hashCode") as KFunction<Int>
    val intToString = (Int::class.java).toLiteKClass().getMemberByName("toString") as KFunction<String>
    assert(intEquals.call(-1, -1))
    assert(intHashCode.call(0) != Int::hashCode.call(1))
    assert(intToString.call(42) == "42")

    //val stringEquals = String::class.java.getFunctionByName("equals") as KFunction<Boolean>
//    val stringHashcode = String::class.java.getFunctionByName("hashCode") as KFunction<Int>
//    val stringToString = String::class.java.getFunctionByName("toString") as KFunction<String>
//    assert(stringEquals.call("beer", "beer"))
//    stringHashcode.call("beer")

    //return stringToString.call("OK")
    return "OK"
}
