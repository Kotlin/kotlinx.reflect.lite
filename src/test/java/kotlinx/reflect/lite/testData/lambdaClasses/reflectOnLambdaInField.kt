package tests.lambdaClasses.reflectOnLambdaInField

import kotlin.reflect.jvm.reflect

class C {
    val x = { OK: String -> }
}

fun box(): String {
    return C().x.reflect()?.parameters?.singleOrNull()?.name ?: "null"
}