package nativebench
import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole

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
  def slinc: String = ctimeSlincBenchHelper.run
  @Benchmark
  def jni: String = jniInstance.run()
}

@State(Scope.Thread)
class QSortState {
  var values: Array[Int] = null
  @Setup
  def setup(): Unit =
    values = Array.fill(1_000)(scala.util.Random.nextInt())
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
  def jniNativeQSort(bh: Blackhole, state: QSortState): Unit =
    jniInstance.callNativeIntQsort(state.values)
  @Benchmark
  def jniQSort(bh: Blackhole, state: QSortState): Unit =
    jniInstance.setup()
    bh.consume(jniInstance.callIntQsort(state.values))

  @Benchmark
  def slincQSortWithoutCopyBack(bh: Blackhole, state: QSortState): Unit =
    bh.consume(ctimeSlincBenchHelper.runQsortWithoutCopyBack(state.values))
  @Benchmark
  def slincQSortWithCopyBack(bh: Blackhole, state: QSortState): Array[Int] =
    ctimeSlincBenchHelper.runQsortWithCopyBack(state.values)
}
