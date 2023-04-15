package nativebench
import fr.hammons.slinc.*
import fr.hammons.slinc.types.*
import fr.hammons.slinc.runtime.{*, given}
import fr.hammons.slinc.fset.FSetBacking

trait benchBinding derives FSet:
  def ctime(time: Ptr[Long]): Ptr[Byte]
  def sscanf(buffer: Ptr[Byte], format: Ptr[Byte], args: Seq[Variadic]): CInt
  def mktime(tm: Ptr[tm]): Long
  def qsort(
      data: Ptr[Any],
      num: Long,
      size: SizeT,
      compare: Ptr[(Ptr[Any], Ptr[Any]) => Int]
  ): Unit

object ctimeSlincBenchHelper:
  val ELM_SIZE = sizeOf[Int]
  def runQsortWithoutCopyBack(
      benchBinding: benchBindingType,
      arr: Array[Int],
      cb: Ptr[(Ptr[Any], Ptr[Any]) => Int]
  ): Unit =
    Scope.confined {
      val data = Ptr.copy(arr)
      benchBinding.qsort(
        data.castTo[Any],
        arr.length.toLong,
        ELM_SIZE,
        cb
      )
    }
  def runQsortAllocCallbackForEachIteration(
      benchBinding: benchBindingType,
      arr: Array[Int]
  ): Unit =
    Scope.confined {
      val data = Ptr.copy(arr)

      val cb = Ptr.upcall[(Ptr[Any], Ptr[Any]) => Int] { (iptr0, iptr1) =>
        val i = !iptr0.castTo[Int]
        val j = !iptr1.castTo[Int]
        i - j
      }
      benchBinding.qsort(
        data.castTo[Any],
        arr.length,
        ELM_SIZE,
        cb
      )
    }
  def runQsortWithCopyBack(
      benchBinding: benchBindingType,
      arr: Array[Int],
      cb: Ptr[(Ptr[Any], Ptr[Any]) => Int]
  ): Array[CInt] =
    Scope.confined {
      val data = Ptr.copy(arr)
      benchBinding.qsort(
        data.castTo[Any],
        arr.length.toLong,
        ELM_SIZE,
        cb
      )
      data.asArray(arr.length).unsafeArray
    }
  type benchBindingType = FSetBacking[benchBinding] {
    def ctime(time: Ptr[Long]): Ptr[Byte];
    def sscanf(buffer: Ptr[Byte], format: Ptr[Byte], args: Seq[Variadic]): Int;
    def mktime(tm: Ptr[nativebench.tm]): Long;
    def qsort(
        data: Ptr[Any],
        num: Long,
        size: SizeT,
        compare: Ptr[(Ptr[Any], Ptr[Any]) => Int]
    ): Unit
  }
  implicit val tmStructEv: Struct[tm] = Struct.derived[tm]
  def run(benchBinding: benchBindingType) =
    Scope.confined {
      // copy string into ptr(jvm to native)
      val src = "2022-04-01 00:42";
      val fmt = "%04d-%02d-%02d %02d:%02d"
      val srcptr = Ptr.copy(src)
      val fmtptr = Ptr.copy(fmt)

      val (year, month, hour, day, min) = (
        Ptr.blank[Int],
        Ptr.blank[Int],
        Ptr.blank[Int],
        Ptr.blank[Int],
        Ptr.blank[Int]
      )

      // invoke call
      benchBinding.sscanf(
        srcptr,
        fmtptr,
        Seq(year, month, day, hour, min)
      )

      // dereferencing pointer (native to jvm)
      val t = nativebench.tm(
        0,
        !min,
        !hour,
        !day,
        (!month) - 1,
        (!year) - 1900,
        5,
        91,
        -1
      )
      // copy object (jvm to native)
      val tptr = Ptr.copy(t)
      val time = benchBinding.mktime(tptr)
      val timeptr = Ptr.copy(time)

      // read string (native to jvm)
      benchBinding.ctime(timeptr).copyIntoString(100)
    }

object benchBinding:
  import fr.hammons.slinc.runtime.given
  final val instance = FSet.instance[benchBinding]
