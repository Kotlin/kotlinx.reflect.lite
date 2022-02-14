package tests.callBy.simpleConstructor

class A(val result: String = "OK")

fun box(): String = ::A.callBy(mapOf()).result