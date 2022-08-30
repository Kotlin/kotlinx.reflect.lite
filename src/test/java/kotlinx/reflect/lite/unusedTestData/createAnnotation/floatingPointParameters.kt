/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */


package tests.createAnnotation.floatingPointParameters
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

annotation class D(val d: Double)
annotation class F(val f: Float)

/*
// TODO: uncomment once KT-13887 is implemented
@D(Double.NaN)
fun dnan() {}

@F(Float.NaN)
fun fnan() {}
*/

@D(-0.0)
fun dMinusZero() {}
@D(+0.0)
fun dPlusZero() {}

@F(-0.0f)
fun fMinusZero() {}
@F(+0.0f)
fun fPlusZero() {}

fun check(x: Any, y: Any, regexString: String) {
    assertEquals(x, y)
    assertEquals(y, x)
    assertEquals(x.hashCode(), y.hashCode())

    val regex = regexString.toRegex()
    assertTrue(regex.matches(x.toString()))
    assertTrue(regex.matches(y.toString()))
}

fun checkNot(x: Any, y: Any) {
    assertNotEquals(x, y)
    assertNotEquals(y, x)
    assertNotEquals(x.hashCode(), y.hashCode())
    assertNotEquals(x.toString(), y.toString())
}

fun box(): String {
/*
    check(::dnan.annotations.single() as D, D::class.constructors.single().call(Double.NaN))
    check(::fnan.annotations.single() as F, F::class.constructors.single().call(Float.NaN))
*/

    val dmz = D::class.constructors.single().call(-0.0)
    val dpz = D::class.constructors.single().call(+0.0)
    val fmz = F::class.constructors.single().call(-0.0f)
    val fpz = F::class.constructors.single().call(+0.0f)
    check(::dMinusZero.annotations.single() as D, dmz, "@tests.createAnnotation.floatingPointParameters.D\\(d=-0.0\\)")
    check(::dPlusZero.annotations.single() as D, dpz, "@tests.createAnnotation.floatingPointParameters.D\\(d=0.0\\)")
    check(::fMinusZero.annotations.single() as F, fmz, "@tests.createAnnotation.floatingPointParameters.F\\(f=-0.0f?\\)")
    check(::fPlusZero.annotations.single() as F, fpz, "@tests.createAnnotation.floatingPointParameters.F\\(f=0.0f?\\)")

    checkNot(dmz, dpz)
    checkNot(fmz, fpz)
    checkNot(dmz, fmz)
    checkNot(dpz, fpz)

    return "OK"
}
