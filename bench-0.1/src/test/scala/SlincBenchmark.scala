package nativebench
import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import fr.hammons.slinc.Ptr

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(
  value = 1,
  jvmArgs = Array(
    "-Xmx2G"
    // "--enable-native-access=ALL-UNNAMED",
    // "--add-modules=jdk.incubator.foreign"
  )
)
@Threads(2)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
class NativeBenchmarks {

  var length: Int = _;
  var jniInstance: ctimeJNIBenchHelper = _
  @Setup
  def setup(): Unit =
    jniInstance = ctimeJNIBenchHelper()
  @Benchmark
  def ctimeSlinc: String = ctimeSlincBenchHelper.run
  @Benchmark
  def ctimeJNI: String = jniInstance.run()
}

@State(Scope.Thread)
class QSortState {
  var values: Array[Int] = null
  var callbackHandle: Ptr[(Ptr[Any], Ptr[Any]) => Int] = _
  @Setup
  def setup(): Unit =
    import fr.hammons.slinc.runtime.given
    values = Array.fill(1_000)(scala.util.Random.nextInt())
    callbackHandle = fr.hammons.slinc.Scope.global {

      Ptr.upcall[(Ptr[Any], Ptr[Any]) => Int] { (iptr0, iptr1) =>
        val i = !iptr0.castTo[Int]
        val j = !iptr1.castTo[Int]
        i - j
      }
    }

}

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(
  value = 1,
  jvmArgs = Array(
    "-Xmx2G"
    // "--enable-native-access=ALL-UNNAMED",
    // "--add-modules=jdk.incubator.foreign"
  )
)
@Threads(1) // Threads >= 2 causes error :(
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
class SimpleNativeCallBenchmarks {

  var length: Int = _;
  var jniInstance: ctimeJNIBenchHelper = _
  @Setup
  def setup(): Unit =
    jniInstance = ctimeJNIBenchHelper()
  @Benchmark
  def slincQSortJVM(bh: Blackhole, state: QSortState): Unit =
    bh.consume(scala.util.Sorting.quickSort(state.values))
  @Benchmark
  def jniNativeQSort(bh: Blackhole, state: QSortState): Unit =
    jniInstance.callNativeIntQsort(state.values)
  @Benchmark
  def jniQSort(bh: Blackhole, state: QSortState): Unit =
    jniInstance.setup()
    bh.consume(jniInstance.callIntQsort(state.values))
  @Benchmark
  def slincQsortAllocCallbackForEachIteration(
      bh: Blackhole,
      state: QSortState
  ): Unit =
    bh.consume(
      ctimeSlincBenchHelper.runQsortAllocCallbackForEachIteration(state.values)
    )
  @Benchmark
  def slincQSortWithoutCopyBack(bh: Blackhole, state: QSortState): Unit =
    bh.consume(
      ctimeSlincBenchHelper.runQsortWithoutCopyBack(
        state.values,
        state.callbackHandle
      )
    )
  @Benchmark
  def slincQSortWithCopyBack(bh: Blackhole, state: QSortState): Array[Int] =
    ctimeSlincBenchHelper.runQsortWithCopyBack(
      state.values,
      state.callbackHandle
    )
}
