package tests.call.bound.extensionFunction

import kotlin.reflect.*

fun String.foo(x: String) = this + x
fun String?.bar(x: String) = x

fun box() =
        (""::foo).call("O") + (null::bar).call("K")