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

import kotlinx.metadata.ClassName
import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.metadata.jvm.JvmFieldSignature
import kotlinx.metadata.jvm.JvmMemberSignature
import kotlinx.metadata.jvm.JvmMethodSignature
import kotlinx.reflect.lite.*
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

internal class ClassMetadataImpl(
    private val flags: Flags,
    private val name: ClassName,
    override val functions: Collection<FunctionMetadata>,
    override val constructors: Collection<ConstructorMetadata>,
    override val properties: Collection<PropertyMetadata>,
    private val functionsBySignature: Map<JvmMemberSignature, FunctionMetadata>,
    private val constructorsBySignature: Map<JvmMemberSignature, ConstructorMetadata>,
    private val propertiesBySignature: Map<JvmMemberSignature, PropertyMetadata>
) : ClassMetadata {
    override fun getFunction(method: Method): FunctionMetadata? =
        functionsBySignature[signature(method.name, method.parameterTypes, method.returnType)]

    override fun getConstructor(constructor: Constructor<*>): ConstructorMetadata? =
        constructorsBySignature[signature("<init>", constructor.parameterTypes, Void.TYPE)]

    override fun getProperty(field: Field): PropertyMetadata? =
        propertiesBySignature[JvmFieldSignature(field.name, field.type.desc())]

    private fun signature(name: String, parameterTypes: Array<Class<*>>, returnType: Class<*>): JvmMemberSignature =
        JvmMethodSignature(
            name,
            parameterTypes.joinToString(
                separator = "", prefix = "(", postfix = ")${returnType.desc()}", transform = Class<*>::desc
            )
        )

    override val isData: Boolean
        get() = Flag.Class.IS_DATA(flags)

    override val kind: ClassMetadata.Kind
        get() = when {
            Flag.Class.IS_CLASS(flags) -> ClassMetadata.Kind.CLASS
            Flag.Class.IS_INTERFACE(flags) -> ClassMetadata.Kind.INTERFACE
            Flag.Class.IS_ENUM_CLASS(flags) -> ClassMetadata.Kind.ENUM_CLASS
            Flag.Class.IS_ENUM_ENTRY(flags) -> {
                // Enum entries were never supposed to have their own class kind, so we're treating them
                // as other anonymous classes, i.e. as CLASS
                ClassMetadata.Kind.CLASS
            }
            Flag.Class.IS_ANNOTATION_CLASS(flags) -> ClassMetadata.Kind.ANNOTATION_CLASS
            Flag.Class.IS_OBJECT(flags) -> ClassMetadata.Kind.OBJECT
            Flag.Class.IS_COMPANION_OBJECT(flags) -> ClassMetadata.Kind.COMPANION_OBJECT
            else -> error("Unknown class kind for class $name")
        }

    override val visibility: DeclarationMetadata.Visibility?
        get() = flags.toVisibility
}
