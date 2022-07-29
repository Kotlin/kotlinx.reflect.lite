package tests.callBy.inlineClassDefaultArguments

inline class A(val x: Int)

fun test(x: A = A(0)) = "OK"

fun box(): String {
    return (::test).callBy(mapOf())
}