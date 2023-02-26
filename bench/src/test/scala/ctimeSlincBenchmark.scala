package nativebench
import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(
  value = 1,
  jvmArgs = Array(
    "-Xmx2G",
    "--enable-native-access=ALL-UNNAMED",
    "--add-modules=jdk.incubator.foreign"
  )
)
@Threads(2)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
class NativeBenchmarks {
  var jniInstance:ctimeJNIBenchHelper = _
  @Setup
  def setup():Unit =
    jniInstance = ctimeJNIBenchHelper()
  @Benchmark
  def slinc: String = ctimeSlincBenchHelper.run
  @Benchmark
  def jni: String = jniInstance.run()
}
