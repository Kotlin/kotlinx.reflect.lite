package tests.functions.isAccessibleOnAllMembers

import kotlin.reflect.jvm.isAccessible

fun box(): String {
    val members = Observer::class.members
    for (member in members) {
        member.isAccessible = true
    }
    return members.single { it.name == "result" }.call(Observer()) as String
}

class Observer : AutoCloseable {
    override fun close() {
    }

    private fun result() = "OK"
}