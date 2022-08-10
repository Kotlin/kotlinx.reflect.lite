package tests.parameters.propertySetter

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

var default: Int = 0

var defaultAnnotated: Int = 0
    public set

var custom: Int = 0
    set(myName: Int) {}

fun checkPropertySetterParam(property: KMutableProperty0<*>, name: String?) {
    val parameter = property.setter.parameters.single()
    assertEquals(0, parameter.index)
    assertEquals(name, parameter.name)
}

fun box(): String {
    val clazz = Class.forName("tests.parameters.propertySetter.PropertySetterKt").kotlinPackage
    checkPropertySetterParam(clazz.getMemberByName("default") as KMutableProperty0<Int>, null)
    checkPropertySetterParam(clazz.getMemberByName("defaultAnnotated") as KMutableProperty0<Int>, null)
    checkPropertySetterParam(clazz.getMemberByName("custom") as KMutableProperty0<Int>, "myName")

    return "OK"
}
