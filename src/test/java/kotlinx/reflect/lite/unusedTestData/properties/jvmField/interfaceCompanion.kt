/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.jvmField.interfaceCompanion

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.companionObject

class Bar(val value: String)

interface Foo {

    companion object {
        @JvmField
        val z = Bar("OK")
    }
}


fun box(): String {
    val field = Foo::class.companionObject!!.memberProperties.single() as KProperty1<Foo.Companion, Bar>
    return field.get(Foo.Companion).value
}
