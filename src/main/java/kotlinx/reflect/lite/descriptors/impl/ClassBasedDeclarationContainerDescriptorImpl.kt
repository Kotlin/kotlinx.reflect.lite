// Most logic copied from: https://github.com/JetBrains/kotlin/blob/master/core/reflection.jvm/src/kotlin/reflect/jvm/internal/KDeclarationContainerImpl.kt
package kotlinx.reflect.lite.descriptors.impl

import kotlinx.reflect.lite.builtins.*
import kotlinx.reflect.lite.descriptors.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.impl.KotlinReflectionInternalError
import kotlinx.reflect.lite.misc.*
import java.lang.reflect.*

internal abstract class ClassBasedDeclarationContainerDescriptorImpl(
    override val jClass: Class<*>
) : ClassBasedDeclarationContainerDescriptor {
    protected open val methodOwner: Class<*>
        get() = jClass.wrapperByPrimitive ?: jClass

    abstract val memberScope: MemberScope
    abstract val staticScope: MemberScope

    private val declaredNonStaticMembers: Collection<KCallableImpl<*>>
        get() = getMembers(memberScope, MemberBelonginess.DECLARED)

    private val declaredStaticMembers: Collection<KCallableImpl<*>>
        get() = getMembers(staticScope, MemberBelonginess.DECLARED)

    private val inheritedNonStaticMembers: Collection<KCallableImpl<*>>
        get() = getMembers(memberScope, MemberBelonginess.INHERITED)

    private val inheritedStaticMembers: Collection<KCallableImpl<*>>
        get() = getMembers(staticScope, MemberBelonginess.INHERITED)

    private val allNonStaticMembers: Collection<KCallableImpl<*>>
            by lazy { declaredNonStaticMembers + inheritedNonStaticMembers }
    private val allStaticMembers: Collection<KCallableImpl<*>>
            by lazy { declaredStaticMembers + inheritedStaticMembers }
    override val declaredMembers: Collection<KCallableImpl<*>>
            by lazy { declaredNonStaticMembers + declaredStaticMembers }
    override val allMembers: Collection<KCallableImpl<*>>
            by lazy { allNonStaticMembers + allStaticMembers }

    private fun getMembers(scope: MemberScope, belonginess: MemberBelonginess): Collection<KCallableImpl<*>> =
        (scope.functions + scope.properties).mapNotNull { descriptor ->
            if (belonginess.accept(descriptor)) createKCallable(descriptor) else null
        }.toList()

    protected enum class MemberBelonginess {
        DECLARED,
        INHERITED;

        fun accept(member: CallableDescriptor): Boolean =
            member.isReal == (this == DECLARED)
    }

    private fun Class<*>.lookupMethod(
        name: String, parameterTypes: Array<Class<*>>, returnType: Class<*>, isStaticDefault: Boolean
    ): Method? {
        // Static "$default" method in any class takes an instance of that class as the first parameter.
        if (isStaticDefault) {
            parameterTypes[0] = this
        }

        tryGetMethod(name, parameterTypes, returnType)?.let { return it }

        superclass?.lookupMethod(name, parameterTypes, returnType, isStaticDefault)?.let { return it }

        // TODO: avoid exponential complexity here
        for (superInterface in interfaces) {
            superInterface.lookupMethod(name, parameterTypes, returnType, isStaticDefault)?.let { return it }

            // Static "$default" methods should be looked up in each DefaultImpls class, see KT-33430
            if (isStaticDefault) {
                val defaultImpls = superInterface.safeClassLoader.tryLoadClass(superInterface.name + JvmAbi.DEFAULT_IMPLS_SUFFIX)
                if (defaultImpls != null) {
                    parameterTypes[0] = superInterface
                    defaultImpls.tryGetMethod(name, parameterTypes, returnType)?.let { return it }
                }
            }
        }

        return null
    }

    private fun Class<*>.tryGetMethod(name: String, parameterTypes: Array<Class<*>>, returnType: Class<*>): Method? =
        try {
            val result = getDeclaredMethod(name, *parameterTypes)

            if (result.returnType == returnType) result
            else {
                // If we've found a method with an unexpected return type, it's likely that there are several methods in this class
                // with the given parameter types and Java reflection API has returned not the one we're looking for.
                // Falling back to enumerating all methods in the class in this (rather rare) case.
                // Example: class A(val x: Int) { fun getX(): String = ... }
                declaredMethods.firstOrNull { method ->
                    method.name == name && method.returnType == returnType && method.parameterTypes.contentEquals(parameterTypes)
                }
            }
        } catch (e: NoSuchMethodException) {
            null
        }

    private fun Class<*>.tryGetConstructor(parameterTypes: List<Class<*>>): Constructor<*>? =
        try {
            getDeclaredConstructor(*parameterTypes.toTypedArray())
        } catch (e: NoSuchMethodException) {
            null
        }

    override fun findMethodBySignature(name: String, desc: String): Method? {
        if (name == "<init>") return null

        val parameterTypes = loadParameterTypes(desc).toTypedArray()
        val returnType = loadReturnType(desc)
        methodOwner.lookupMethod(name, parameterTypes, returnType, false)?.let { return it }

        // Methods from java.lang.Object (equals, hashCode, toString) cannot be found in the interface via
        // Class.getMethod/getDeclaredMethod, so for interfaces, we also look in java.lang.Object.
        if (methodOwner.isInterface) {
            Any::class.java.lookupMethod(name, parameterTypes, returnType, false)?.let { return it }
        }

        return null
    }

    override fun findDefaultMethod(name: String, desc: String, isMember: Boolean): Method? {
        if (name == "<init>") return null

        val parameterTypes = arrayListOf<Class<*>>()
        if (isMember) {
            // Note that this value is replaced inside the lookupMethod call below, for each class/interface in the hierarchy.
            parameterTypes.add(jClass)
        }
        addParametersAndMasks(parameterTypes, desc, false)

        return methodOwner.lookupMethod(
            name + JvmAbi.DEFAULT_PARAMS_IMPL_SUFFIX, parameterTypes.toTypedArray(), loadReturnType(desc), isStaticDefault = isMember
        )
    }

    override fun findConstructorBySignature(desc: String): Constructor<*>? =
        jClass.tryGetConstructor(loadParameterTypes(desc))

    override fun findDefaultConstructor(desc: String): Constructor<*>? =
        jClass.tryGetConstructor(arrayListOf<Class<*>>().also { parameterTypes ->
            addParametersAndMasks(parameterTypes, desc, true)
        })



    private fun addParametersAndMasks(result: MutableList<Class<*>>, desc: String, isConstructor: Boolean) {
        val valueParameters = loadParameterTypes(desc)
        result.addAll(valueParameters)
        repeat((valueParameters.size + Integer.SIZE - 1) / Integer.SIZE) {
            result.add(Integer.TYPE)
        }
        result.add(if (isConstructor) DEFAULT_CONSTRUCTOR_MARKER else Any::class.java)
    }

    private fun loadParameterTypes(desc: String): List<Class<*>> {
        val result = arrayListOf<Class<*>>()

        var begin = 1
        while (desc[begin] != ')') {
            var end = begin
            while (desc[end] == '[') end++
            @Suppress("SpellCheckingInspection")
            when (desc[end]) {
                in "VZCBSIFJD" -> end++
                'L' -> end = desc.indexOf(';', begin) + 1
                else -> throw KotlinReflectionInternalError("Unknown type prefix in the method signature: $desc")
            }

            result.add(parseType(desc, begin, end))
            begin = end
        }

        return result
    }

    private fun parseType(desc: String, begin: Int, end: Int): Class<*> =
        when (desc[begin]) {
            'L' -> jClass.safeClassLoader.loadClass(desc.substring(begin + 1, end - 1).replace('/', '.'))
            '[' -> parseType(desc, begin + 1, end).createArrayType()
            'V' -> Void.TYPE
            'Z' -> Boolean::class.java
            'C' -> Char::class.java
            'B' -> Byte::class.java
            'S' -> Short::class.java
            'I' -> Int::class.java
            'F' -> Float::class.java
            'J' -> Long::class.java
            'D' -> Double::class.java
            else -> throw KotlinReflectionInternalError("Unknown type prefix in the method signature: $desc")
        }

    private fun loadReturnType(desc: String): Class<*> =
        parseType(desc, desc.indexOf(')') + 1, desc.length)

    companion object {
        private val DEFAULT_CONSTRUCTOR_MARKER = Class.forName("kotlin.jvm.internal.DefaultConstructorMarker")
    }

    override fun equals(other: Any?): Boolean {
        return other is ClassBasedDeclarationContainerDescriptor && jClass == other.jClass
    }

    override fun hashCode(): Int = jClass.hashCode()
}
