/*
 * Copyright 2010-2017 JetBrains s.r.o.
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

import kotlinx.reflect.lite.ClassMetadata
import kotlinx.reflect.lite.ConstructorMetadata
import kotlinx.reflect.lite.FunctionMetadata
import org.jetbrains.kotlin.serialization.ProtoBuf
import org.jetbrains.kotlin.serialization.deserialization.NameResolver
import org.jetbrains.kotlin.serialization.deserialization.TypeTable
import org.jetbrains.kotlin.serialization.jvm.JvmProtoBufUtil
import java.lang.reflect.Constructor
import java.lang.reflect.Method

internal class ClassMetadataImpl(
        private val proto: ProtoBuf.Class,
        private val nameResolver: NameResolver
) : ClassMetadata {
    private val typeTable = TypeTable(proto.typeTable)

    private val functions: Map<String, ProtoBuf.Function> by lazySoft {
        proto.functionList.mapNotNull { function ->
            JvmProtoBufUtil.getJvmMethodSignature(function, nameResolver, typeTable)
                    ?.let { it to function }
        }.toMap()
    }

    private val constructors: Map<String, ProtoBuf.Constructor> by lazySoft {
        proto.constructorList.mapNotNull { constructor ->
            JvmProtoBufUtil.getJvmConstructorSignature(constructor, nameResolver, typeTable)
                    ?.let { it to constructor }
        }.toMap()
    }

    override fun getFunction(method: Method): FunctionMetadata? {
        return functions[signature(method.name, method.parameterTypes, method.returnType)]
                ?.let { FunctionMetadataImpl(it, nameResolver) }
    }

    override fun getConstructor(constructor: Constructor<*>): ConstructorMetadata? {
        return constructors[signature("<init>", constructor.parameterTypes, Void.TYPE)]
                ?.let { ConstructorMetadataImpl(it, nameResolver) }
    }

    private fun signature(name: String, parameterTypes: Array<Class<*>>, returnType: Class<*>): String {
        return buildString {
            append(name)
            parameterTypes.joinTo(this, separator = "", prefix = "(", postfix = ")", transform = Class<*>::desc)
            append(returnType.desc())
        }
    }
}
