package tests.builtins.collections

// See KT-11258, KT-16616

import java.util.*
import kotlin.test.assertEquals

fun box(): String {
    listOf(
            ArrayList::class,
            LinkedList::class,
            AbstractList::class,
            HashSet::class,
            TreeSet::class,
            HashMap::class,
            TreeMap::class,
            AbstractMap::class,
            AbstractMap.SimpleEntry::class
    ).map {
        it.members.map(Any::toString)
    }

    assertEquals(1, Collection<Any>::size.getter(listOf(1)))

    return "OK"
}