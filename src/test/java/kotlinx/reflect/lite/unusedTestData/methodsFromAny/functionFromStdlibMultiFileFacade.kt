package tests.methodsFromAny.functionFromStdlibMultiFileFacade

// KT-12630 KotlinReflectionInternalError on referencing some functions from stdlib


import kotlin.test.*

fun box(): String {
    val asIterable = List<Int>::asIterable
    assertEquals("fun kotlin.collections.Iterable<T>.asIterable(): kotlin.collections.Iterable<T>", asIterable.toString())

    val lazyOf: (String) -> Lazy<String> = ::lazyOf
    assertEquals("fun lazyOf(T): kotlin.Lazy<T>", lazyOf.toString())

    return "OK"
}