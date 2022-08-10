package tests.constructors.primaryConstructor

import kotlin.test.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*

class OnlyPrimary

class PrimaryWithSecondary(val s: String) {
    constructor(x: Int) : this(x.toString())

    override fun toString() = s
}

class OnlySecondary {
    constructor(s: String)
}

class TwoSecondaries {
    constructor(s: String)
    constructor(d: Double)
}

enum class En

interface I
object O
class C {
    companion object
}

fun box(): String {
    val p1 = (OnlyPrimary::class.java.kotlinClass as KClass<OnlyPrimary>).primaryConstructor
    assertNotNull(p1)
    assert(p1.call() is OnlyPrimary)

    val p2 = (PrimaryWithSecondary::class.java.kotlinClass as KClass<PrimaryWithSecondary>).primaryConstructor
    assertNotNull(p2)
    assert(p2.call("beer").toString() == "beer")

    val p3 = (OnlySecondary::class.java.kotlinClass as KClass<OnlySecondary>).primaryConstructor
    assertNull(p3)

    val p4 = (TwoSecondaries::class.java.kotlinClass as KClass<TwoSecondaries>).primaryConstructor
    assertNull(p4)

    assertNotNull((En::class.java.kotlinClass as KClass<En>).primaryConstructor)

    assertNull((I::class.java.kotlinClass as KClass<I>).primaryConstructor)
    assertNull((O::class.java.kotlinClass as KClass<O>).primaryConstructor)
    assertNull((C.Companion::class.java.kotlinClass as KClass<C.Companion>).primaryConstructor)

    assertNull((object {}::class.java.kotlinClass as KClass<Any>).primaryConstructor)

    return "OK"
}
