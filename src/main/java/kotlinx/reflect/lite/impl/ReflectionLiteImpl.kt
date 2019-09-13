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

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*
import kotlinx.reflect.lite.ClassMetadata
import kotlinx.reflect.lite.ParameterMetadata
import kotlinx.reflect.lite.TypeMetadata

internal object ReflectionLiteImpl {
    fun loadClassMetadata(klass: Class<*>): ClassMetadata? {
        val header = with(klass.getAnnotation(Metadata::class.java)) {
            KotlinClassHeader(kind, metadataVersion, bytecodeVersion, data1, data2, extraString, packageName, extraInt)
        }

        val metadata = KotlinClassMetadata.read(header)
        if (metadata is KotlinClassMetadata.Class) {
            return ReflectLiteClassVisitor().also(metadata::accept).createMetadata()
        }

        return null
    }
}

internal class ReflectLiteClassVisitor : KmClassVisitor() {
    private var flags: Flags? = null
    private lateinit var name: ClassName
    private val functions = mutableListOf<ReflectLiteFunctionVisitor>()
    private val constructors = mutableListOf<ReflectLiteConstructorVisitor>()
    private val properties = mutableListOf<ReflectLitePropertyVisitor>()

    override fun visit(flags: Flags, name: ClassName) {
        this.flags = flags
        this.name = name
    }

    override fun visitFunction(flags: Flags, name: String): KmFunctionVisitor? =
        ReflectLiteFunctionVisitor(flags, name).also(functions::add0)

    override fun visitConstructor(flags: Flags): KmConstructorVisitor? =
        ReflectLiteConstructorVisitor(flags).also(constructors::add0)

    override fun visitProperty(flags: Flags, name: String, getterFlags: Flags, setterFlags: Flags): KmPropertyVisitor? =
        ReflectLitePropertyVisitor(flags, name).also(properties::add0)

    fun createMetadata(): ClassMetadata {
        val functions = functions.map(ReflectLiteFunctionVisitor::createMetadata)
        val properties = properties.map(ReflectLitePropertyVisitor::createMetadata)
        val constructors = constructors.map(ReflectLiteConstructorVisitor::createMetadata)
        return ClassMetadataImpl(
            flags!!, name, functions, constructors, properties,
            functions.mapNotNull { it.signature?.to(it) }.toMap(),
            constructors.mapNotNull { it.signature?.to(it) }.toMap(),
            properties.mapNotNull { it.fieldSignature?.to(it) }.toMap()
        )
    }
}

internal class ReflectLiteFunctionVisitor(
    private var flags: Flags,
    private var name: String
) : KmFunctionVisitor() {
    private var signature: JvmMethodSignature? = null
    private val valueParameters = mutableListOf<ReflectLiteValueParameterVisitor>()
    private var receiverParameterType: ReflectLiteTypeVisitor? = null
    private lateinit var returnType: ReflectLiteTypeVisitor

    override fun visitReceiverParameterType(flags: Flags): KmTypeVisitor? =
        ReflectLiteTypeVisitor(flags).also { receiverParameterType = it }

    override fun visitValueParameter(flags: Flags, name: String): KmValueParameterVisitor? =
        ReflectLiteValueParameterVisitor(flags, name).also(valueParameters::add0)

    override fun visitReturnType(flags: Flags): KmTypeVisitor? =
        ReflectLiteTypeVisitor(flags).also { returnType = it }

    override fun visitExtensions(type: KmExtensionType): KmFunctionExtensionVisitor? =
        object : JvmFunctionExtensionVisitor() {
            override fun visit(desc: JvmMethodSignature?) {
                signature = desc
            }
        }

    fun createMetadata(): FunctionMetadataImpl =
        FunctionMetadataImpl(
            flags, name,
            receiverParameterType?.createMetadata(),
            valueParameters.map(ReflectLiteValueParameterVisitor::createMetadata),
            returnType.createMetadata(),
            signature
        )
}

internal class ReflectLiteConstructorVisitor(
    private var flags: Flags
) : KmConstructorVisitor() {
    private var signature: JvmMethodSignature? = null
    private val valueParameters = mutableListOf<ReflectLiteValueParameterVisitor>()

    override fun visitValueParameter(flags: Flags, name: String): KmValueParameterVisitor? =
        ReflectLiteValueParameterVisitor(flags, name).also(valueParameters::add0)

    override fun visitExtensions(type: KmExtensionType): KmConstructorExtensionVisitor? =
        object : JvmConstructorExtensionVisitor() {
            override fun visit(desc: JvmMethodSignature?) {
                signature = desc
            }
        }

    fun createMetadata(): ConstructorMetadataImpl =
        ConstructorMetadataImpl(
            flags,
            valueParameters.map(ReflectLiteValueParameterVisitor::createMetadata),
            signature
        )
}

internal class ReflectLitePropertyVisitor(
    private var flags: Flags,
    private var name: String
) : KmPropertyVisitor() {
    private var field: JvmFieldSignature? = null
    private var getter: JvmMethodSignature? = null
    private var setter: JvmMethodSignature? = null
    private var receiverParameterType: ReflectLiteTypeVisitor? = null
    private lateinit var returnType: ReflectLiteTypeVisitor

    override fun visitReceiverParameterType(flags: Flags): KmTypeVisitor? =
        ReflectLiteTypeVisitor(flags).also { receiverParameterType = it }

    override fun visitReturnType(flags: Flags): KmTypeVisitor? =
        ReflectLiteTypeVisitor(flags).also { returnType = it }

    override fun visitExtensions(type: KmExtensionType): KmPropertyExtensionVisitor? =
        object : JvmPropertyExtensionVisitor() {
            override fun visit(fieldDesc: JvmFieldSignature?, getterDesc: JvmMethodSignature?, setterDesc: JvmMethodSignature?) {
                field = fieldDesc
                getter = getterDesc
                setter = setterDesc
            }
        }

    fun createMetadata(): PropertyMetadataImpl =
        PropertyMetadataImpl(
            flags, name,
            receiverParameterType?.createMetadata(),
            returnType.createMetadata(),
            field
        )
}

internal class ReflectLiteValueParameterVisitor(
    private var flags: Flags,
    private var name: String
) : KmValueParameterVisitor() {
    private lateinit var type: ReflectLiteTypeVisitor

    override fun visitType(flags: Flags): KmTypeVisitor? =
        ReflectLiteTypeVisitor(flags).also { type = it }

    // TODO: vararg

    fun createMetadata(): ParameterMetadata =
        ParameterMetadataImpl(flags, name, type.createMetadata())
}

internal class ReflectLiteTypeVisitor(
    private var flags: Flags
) : KmTypeVisitor() {
    fun createMetadata(): TypeMetadata =
        TypeMetadataImpl(flags)
}
