/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kotlinx.reflect.lite.impl

import org.jetbrains.kotlin.serialization.ProtoBuf
import java.io.InputStream

internal class StringTable(
        private val strings: ProtoBuf.StringTable,
        private val qualifiedNames: ProtoBuf.QualifiedNameTable
) {
    fun getString(index: Int) = strings.getString(index)

    fun getInternalName(index: Int): String {
        val qualifiedName = qualifiedNames.getQualifiedName(index)
        val shortName = getString(qualifiedName.shortName)
        if (!qualifiedName.hasParentQualifiedName()) {
            return shortName
        }
        return getInternalName(qualifiedName.parentQualifiedName) + "/" + shortName
    }

    companion object {
        fun read(input: InputStream) = StringTable(
                ProtoBuf.StringTable.parseDelimitedFrom(input),
                ProtoBuf.QualifiedNameTable.parseDelimitedFrom(input)
        )
    }
}
