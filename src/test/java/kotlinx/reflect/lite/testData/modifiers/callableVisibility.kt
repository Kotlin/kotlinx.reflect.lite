/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.modifiers.callableVisibility

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

open class Foo<in T> {
    public fun publicFun() {}
    protected fun protectedFun() {}
    internal fun internalFun() {}
    private fun privateFun() {}
    private fun privateToThisFun(): T = null!!

    fun getPublicFun() = this::class.java.kotlin.getMemberByName("publicFun")
    fun getInternalFun() = this::class.java.kotlin.getMemberByName("internalFun")
    fun getProtectedFun() = this::class.java.kotlin.getMemberByName("protectedFun")
    fun getPrivateFun() = this::class.java.kotlin.getMemberByName("privateFun")
    fun getPrivateToThisFun(): KFunction<*> = this::class.java.kotlin.getMemberByName("privateToThisFun") as KFunction<T>

    public val publicVal = Unit
    protected val protectedVar = Unit
    internal val internalVal = Unit
    private val privateVal = Unit
    private val privateToThisVal: T? = null

    fun getPublicVal() = this::class.java.kotlin.getMemberByName("publicVal")
    fun getInternalVal() = this::class.java.kotlin.getMemberByName("internalVal")
    fun getProtectedVar() = this::class.java.kotlin.getMemberByName("protectedVar")
    fun getPrivateVal() = this::class.java.kotlin.getMemberByName("privateVal")
    fun getPrivateToThisVal(): KProperty<*> = this::class.java.kotlin.getMemberByName("privateToThisVal") as KProperty<*>

    public var publicVarPrivateSetter = Unit
        private set

    fun getPublicVarPrivateSetter() = this::class.java.kotlin.getMemberByName("publicVarPrivateSetter") as KMutableProperty1<Foo<*>, Unit>
}

fun box(): String {
    val f = Foo<String>()

    assertEquals(KVisibility.PUBLIC, f.getPublicFun().visibility)
    assertEquals(KVisibility.PROTECTED, f.getProtectedFun().visibility)
    assertEquals(KVisibility.INTERNAL, f.getInternalFun().visibility)
    assertEquals(KVisibility.PRIVATE, f.getPrivateFun().visibility)
    assertEquals(KVisibility.PRIVATE, f.getPrivateToThisFun().visibility)

    assertEquals(KVisibility.PUBLIC, f.getPublicVal().visibility)
    assertEquals(KVisibility.PROTECTED, f.getProtectedVar().visibility)
    assertEquals(KVisibility.INTERNAL, f.getInternalVal().visibility)
    assertEquals(KVisibility.PRIVATE, f.getPrivateVal().visibility)
    assertEquals(KVisibility.PRIVATE, f.getPrivateToThisVal().visibility)

    assertEquals(KVisibility.PUBLIC, f.getPublicVarPrivateSetter().visibility)
    assertEquals(KVisibility.PUBLIC, f.getPublicVarPrivateSetter().getter.visibility)
    assertEquals(KVisibility.PRIVATE, f.getPublicVarPrivateSetter().setter.visibility)

    return "OK"
}
