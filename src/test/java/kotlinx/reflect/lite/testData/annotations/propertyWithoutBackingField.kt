package tests.annotations.propertyWithoutBackingField

annotation class Ann(val value: String)

@Ann("OK")
val property: String
    get() = ""

fun box(): String {
    return (::property.annotations.single() as Ann).value
}