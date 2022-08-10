package tests.mapping.constructor

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*

class K {
    class Nested
    inner class Inner
}

class Secondary {
    constructor(x: Int) {}
}

fun check(f: KFunction<Any>) {
    assert(f.javaMethod == null) { "Fail f method" }
    assert(f.javaConstructor != null) { "Fail f constructor" }
    val c = f.javaConstructor!!

    assert(c.kotlinFunction != null) { "Fail m function" }
    val ff = c.kotlinFunction!!

    assert(f == ff) { "Fail f != ff" }
}

fun box(): String {
    check((K::class.java.kotlin).constructors.first())
    check(((K::class.java.kotlin).nestedClasses.first() as KClass<K.Nested>).constructors.first())
    check(((K::class.java.kotlin).nestedClasses.first() as KClass<K.Inner>).constructors.first())
    check((Secondary::class.java.kotlin).constructors.first())

    return "OK"
}
