package kotlinx.reflect.lite

import kotlinx.reflect.lite.jvm.kotlin as liteKotlin
import kotlin.jvm.kotlin as reflectKotlin
import org.openjdk.jmh.annotations.*
import java.util.concurrent.*

@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
open class CallByBenchmark {

    class A {
        fun foo(x: Int): String {
            require(x == 42)
            return "OK"
        }
    }

    private val callableLite = callableLite()

    @Benchmark
    open fun callByLite(): Any? {
        return callableLite.callBy(mapOf(callableLite.parameters[0] to A(), callableLite.parameters[1] to 42))
    }

    @Benchmark
    open fun callByLiteWithLookup(): Any? {
        val callable = callableLite()
        return callable.callBy(mapOf(callable.parameters[0] to A(), callable.parameters[1] to 42))
    }

    private fun callableLite(): KCallable<*> {
        val liteClass = A::class.java.liteKotlin
        val callable = liteClass.members.single { it.name == "foo" }
        return callable
    }

    private val callableReflect = callableReflect()

    private fun callableReflect() = A::class.java.reflectKotlin.members.single { it.name == "foo" }

    @Benchmark
    open fun callByReflect(): Any? {
        return callableReflect.callBy(mapOf(callableReflect.parameters[0] to A(), callableReflect.parameters[1] to 42))
    }

    @Benchmark
    open fun callByReflectWithLookup(): Any? {
        val callable = callableReflect()
        return callable.callBy(mapOf(callable.parameters[0] to A(), callable.parameters[1] to 42))
    }
}
