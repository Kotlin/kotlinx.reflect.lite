/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.jvmStaticInObjectIncorrectReceiver

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*

object Obj {
    @JvmStatic
    fun foo(s: String) {}

    @JvmStatic
    fun bar() {}

    @JvmStatic
    fun sly(obj: Obj) {}

    operator fun get(name: String) = ((Obj::class.java).kDeclarationContainer as KClass<Obj>).members.single { it.name == name }
}

fun box(): String {
    // This should succeed
    (Obj["foo"]).call(Obj, "")
    (Obj["bar"]).call(Obj)
    (Obj["sly"]).call(Obj, Obj)

    // This shouldn't: first argument should always be Obj
    try {
        (Obj["foo"]).call(null, "")
        return "Fail foo"
    } catch (e: IllegalArgumentException) {}

    try {
        (Obj["bar"]).call("")
        return "Fail bar"
    } catch (e: IllegalArgumentException) {}

    try {
        (Obj["sly"]).call(Obj)
        return "Fail sly 1"
    } catch (e: IllegalArgumentException) {}

    try {
        (Obj["sly"]).call(null, Obj)
        return "Fail sly 2"
    } catch (e: IllegalArgumentException) {}

    return "OK"
}
