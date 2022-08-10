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
    val p1 = (OnlyPrimary::class.java.kDeclarationContainer as KClass<OnlyPrimary>).primaryConstructor
    assertNotNull(p1)
    assert(p1.call() is OnlyPrimary)

    val p2 = (PrimaryWithSecondary::class.java.kDeclarationContainer as KClass<PrimaryWithSecondary>).primaryConstructor
    assertNotNull(p2)
    assert(p2.call("beer").toString() == "beer")

    val p3 = (OnlySecondary::class.java.kDeclarationContainer as KClass<OnlySecondary>).primaryConstructor
    assertNull(p3)

    val p4 = (TwoSecondaries::class.java.kDeclarationContainer as KClass<TwoSecondaries>).primaryConstructor
    assertNull(p4)

    assertNotNull((En::class.java.kDeclarationContainer as KClass<En>).primaryConstructor)

    assertNull((I::class.java.kotlin).primaryConstructor)
    assertNull((O::class.java.kotlin).primaryConstructor)
    assertNull((C.Companion::class.java.kDeclarationContainer as KClass<C.Companion>).primaryConstructor)

    assertNull((object {}::class.java.kDeclarationContainer as KClass<Any>).primaryConstructor)

    return "OK"
}
