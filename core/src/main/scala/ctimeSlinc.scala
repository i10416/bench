package nativebench
import fr.hammons.slinc.*
import fr.hammons.slinc.runtime.{_, given}

object benchBinding derives Library:
  def ctime(time: Ptr[Long]): Ptr[Byte] = Library.binding
  def sscanf(buffer: Ptr[Byte], format: Ptr[Byte], args: Variadic*): Int =
    Library.binding
  def mktime(tm: Ptr[tm]): Long = Library.binding

object ctimeSlincBenchHelper:
  implicit val tmStructEv: Struct[tm] = Struct.derived[tm]
  def run =
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
        year,
        month,
        day,
        hour,
        min
      )

    // dereferencing pointer (native to jvm)
      val t = tm(
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

