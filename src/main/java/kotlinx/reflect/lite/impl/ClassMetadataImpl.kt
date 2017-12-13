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

import kotlinx.reflect.lite.*
import org.jetbrains.kotlin.serialization.Flags
import org.jetbrains.kotlin.serialization.ProtoBuf
import org.jetbrains.kotlin.serialization.deserialization.NameResolver
import org.jetbrains.kotlin.serialization.deserialization.TypeTable
import org.jetbrains.kotlin.serialization.jvm.JvmProtoBufUtil
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

internal class ClassMetadataImpl(
        private val proto: ProtoBuf.Class,
        private val nameResolver: NameResolver
) : ClassMetadata {
    private val typeTable = TypeTable(proto.typeTable)

    private val functionsBySignature: Map<String, ProtoBuf.Function> by lazySoft {
        proto.functionList.mapNotNull { function ->
            JvmProtoBufUtil.getJvmMethodSignature(function, nameResolver, typeTable)
                    ?.let { it to function }
        }.toMap()
    }

    private val constructorsBySignature: Map<String, ProtoBuf.Constructor> by lazySoft {
        proto.constructorList.mapNotNull { constructor ->
            JvmProtoBufUtil.getJvmConstructorSignature(constructor, nameResolver, typeTable)
                    ?.let { it to constructor }
        }.toMap()
    }

    private val propertiesBySignature: Map<JvmProtoBufUtil.PropertySignature, ProtoBuf.Property> by lazySoft {
        proto.propertyList.mapNotNull { property ->
            JvmProtoBufUtil.getJvmFieldSignature(property, nameResolver, typeTable)
                    ?.let { it to property }
        }.toMap()
    }

    override val functions: Collection<FunctionMetadata>
        get() = functionsBySignature.values.map { proto -> FunctionMetadataImpl(proto, nameResolver) }

    override val constructors: Collection<ConstructorMetadata>
        get() = constructorsBySignature.values.map { proto -> ConstructorMetadataImpl(proto, nameResolver) }

    override val properties: Collection<PropertyMetadata>
        get() = propertiesBySignature.values.map { proto -> PropertyMetadataImpl(proto, nameResolver) }

    override fun getFunction(method: Method): FunctionMetadata? {
        return functionsBySignature[signature(method.name, method.parameterTypes, method.returnType)]
                ?.let { FunctionMetadataImpl(it, nameResolver) }
    }

    override fun getConstructor(constructor: Constructor<*>): ConstructorMetadata? {
        return constructorsBySignature[signature("<init>", constructor.parameterTypes, Void.TYPE)]
                ?.let { ConstructorMetadataImpl(it, nameResolver) }
    }

    override fun getProperty(field: Field): PropertyMetadata? {
        return propertiesBySignature[JvmProtoBufUtil.PropertySignature(field.name, field.type.desc())]
                ?.let { PropertyMetadataImpl(it, nameResolver) }
    }

    private fun signature(name: String, parameterTypes: Array<Class<*>>, returnType: Class<*>): String {
        return buildString {
            append(name)
            parameterTypes.joinTo(this, separator = "", prefix = "(", postfix = ")", transform = Class<*>::desc)
            append(returnType.desc())
        }
    }

    override val isData: Boolean
        get() = Flags.IS_DATA.get(proto.flags)

    override val kind: ClassMetadata.Kind
        get() = when (Flags.CLASS_KIND.get(proto.flags)) {
            ProtoBuf.Class.Kind.CLASS -> ClassMetadata.Kind.CLASS
            ProtoBuf.Class.Kind.INTERFACE -> ClassMetadata.Kind.INTERFACE
            ProtoBuf.Class.Kind.ENUM_CLASS -> ClassMetadata.Kind.ENUM_CLASS
            ProtoBuf.Class.Kind.ENUM_ENTRY -> {
                // Enum entries were never supposed to have their own class kind, so we're treating them
                // as other anonymous classes, i.e. as CLASS
                ClassMetadata.Kind.CLASS
            }
            ProtoBuf.Class.Kind.ANNOTATION_CLASS -> ClassMetadata.Kind.ANNOTATION_CLASS
            ProtoBuf.Class.Kind.OBJECT -> ClassMetadata.Kind.OBJECT
            ProtoBuf.Class.Kind.COMPANION_OBJECT -> ClassMetadata.Kind.COMPANION_OBJECT
            null -> error("No class kind for class ${nameResolver.getClassId(proto.fqName)}")
        }

    override val visibility: DeclarationMetadata.Visibility?
        get() = Flags.VISIBILITY.get(proto.flags)?.toVisibility
}