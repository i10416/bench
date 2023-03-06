package nativebench
import com.github.sbt.jni.syntax.NativeLoader
import scala.util.Random

class ctimeJNIBenchHelper extends NativeLoader("jnibench0"):
  type PtrLong = Long
  type PtrInt = Long
  type PtrChar = Long
  type PtrTM = Long

  @native def jniCallDerefInt(ptr: PtrInt): Int
  @native def jniCallDerefLong(ptr: PtrLong): Long
  @native def jniCallAllocEmptyIntPtr(): PtrInt
  @native def jniCallAllocLongPtr(src: Long): PtrLong
  @native def jniCallAllocString(src: String): PtrChar
  @native def jniCallAllocTM(tm: tm): PtrTM
  @native def upcall(): Unit
  @native def callNativeIntQsort(data:Array[Int]):Unit
  @native def callIntQsort(data: Array[Int]): Unit
  @native def jniCallSscanf(
      srcptr: PtrChar,
      fmtptr: PtrChar,
      yearptr: Long,
      monthptr: Long,
      dayptr: Long,
      hourptr: Long,
      minptr: Long
  ): Unit
  @native def jniCallMktime(tptr: Long): Long
  @native def jniCallCtime(timeptr: Long): PtrChar
  @native def jniCallCpStr(charBufPtr: PtrChar, len: Int): String
  @native def setup(): Unit
  def debug(): Unit = println("upcalling")
  def upcallIntCompare(iptr: Long, jptr: Long): Int =
    jniCallDerefInt(iptr) - jniCallDerefInt(jptr)
  def run(): String =
    // copy string into ptr(jvm to native)
    val src = "2022-04-01 00:42";
    val fmt = "%04d-%02d-%02d %02d:%02d"
    val srcptr: Long = jniCallAllocString(src)
    val fmtptr: Long = jniCallAllocString(fmt)

    val (year, month, hour, day, min) = (
      jniCallAllocEmptyIntPtr(),
      jniCallAllocEmptyIntPtr(),
      jniCallAllocEmptyIntPtr(),
      jniCallAllocEmptyIntPtr(),
      jniCallAllocEmptyIntPtr()
    )
    // invoke call
    jniCallSscanf(srcptr, fmtptr, year, month, day, hour, min)
    // dereferencing pointer (native to jvm)
    val t = tm(
      0,
      jniCallDerefInt(min),
      jniCallDerefInt(hour),
      jniCallDerefInt(day),
      (jniCallDerefInt(month)) - 1,
      (jniCallDerefInt(year)) - 1900,
      5,
      91,
      -1
    )

    // copy object (jvm to native)
    val tptr = jniCallAllocTM(t)
    val time = jniCallMktime(tptr)
    val timeptr = jniCallAllocLongPtr(time)
    // read string (native to jvm)
    jniCallCpStr(jniCallCtime(timeptr), 100)

@main def debug() =
  val inst = ctimeJNIBenchHelper()
  inst.setup()
  val arr = Array(9,1,3,8,2,4,6,5,7,0)
  inst.callIntQsort(arr)
  println(arr.toList)
