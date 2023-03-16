## About


slinc vs jni benchmark


### benchmark for simple operations


#### Qsort benchmark


Environment

- Scala 3.2.2
- JVM: OpenJDK Runtime Environment Zulu19.30+11-CA (build 19.0.1+10)
- slinc: 0.1.1-110-7863cb
- Apple clang version 13.1.6 (clang-1316.0.21.2.5)



In general, it is clear that upcall JVM method from native is quite slow.
The following change makes a program extremely slower(check `SimpleNativeCallBenchmarks.
jniNativeQSort` and `SimpleNativeCallBenchmarks.jniQSort`).

```diff
(JNIEnv *jenv, jobject jobj, jintArray jarr){
  jint *arr = (*jenv)->GetIntArrayElements(jenv,jarr, 0);
  jsize len = (*jenv)->GetArrayLength(jenv,jarr);
-  qsort(arr,len,sizeof(int),compare_int);
+  qsort(arr,len,sizeof(int),upcall_compare_int);
  (*jenv)->ReleaseIntArrayElements(jenv,jarr,arr, 0);
  return;
};
```


 There's only a small difference in performance between array copy back and forth and array copy without copy back. See `SimpleNativeCallBenchmarks.slincQSortWithCopyBack	` and `SimpleNativeCallBenchmarks.slincQSortWithoutCopyBack`.


As is mentioned in the comment, I confirmed that `SimpleNativeCallBenchmarks.slincQsortAllocCallbackForEachIteration` is much slower than `slincQSortWithCopyBack` and `slincQSortWithoutCopyBack`. Allocating upcall seems costly operation.


> Having cloned your bench and having the callback allocated once (rather than per benchmark iteration), I see a improvement in performance of Slinc's upcall code to just 2x slower than JNI, rather than 5x slower
>
> https://github.com/markehammons/slinc/issues/81#issuecomment-1457928744

SlinC one is around 2 times slower than JNI one when allocating callback in advance and 5x~ times slower when allocating callback for each iteration.

~~SlinC one is around 5 times slower than JNI one probably because SlincQsort transfers array between JVM and native, but I suspect there are other reasons why SlinC ones are slow because there is not large difference between `slincQSortWithCopyBack` and `slincQSortWithoutCopyBack`, which implies data transfer is not the bottleneck~~



| Benchmark                                                          |   NOTE  | Mode | Cnt | Score       | Error        | Units |
| ------------------------------------------------------------------ | --- | ---- | --- | ----------- | ------------ | ----- |
|SimpleNativeCallBenchmarks.slincQSortJVM    |  | avgt|    5 |     1774.509| ±      4.972|  ns/op|
| SimpleNativeCallBenchmarks.jniNativeQSort                          | Using native comparator. __No upcall__     | avgt | 5   | 4272.838    | ±     50.298 | ns/op |
| SimpleNativeCallBenchmarks.jniQSort                                |  Using upcall. destructively mutate original array    | avgt | 5   | 299570.811  | ±   4542.836 | ns/op |
| SimpleNativeCallBenchmarks.slincQSortWithCopyBack                  |  Using global shared upcall. Copy array back and forth.   | avgt | 5   | 618014.439  | ±   8280.107 | ns/op |
| SimpleNativeCallBenchmarks.slincQSortWithoutCopyBack               |Using upcall. Copy and transfer array but not copy back. | avgt | 5   | 625336.580  | ±  10471.754 | ns/op |
| SimpleNativeCallBenchmarks.slincQsortAllocCallbackForEachIteration | Allocating upcall for each iteration.  | avgt | 5   | 1700443.210 | ± 650331.220 | ns/op |
Feedback from SlinC author(@markehammons)




### benchmark for more complex routine

For the following routine,

1. copy string (jvm to native)
2. invoke native call (jvm to native)
3. dereferencing pointer (native to jvm)
4. copy object (jvm to native)
5. copy object (native to jvm)


with the following environment,

- Scala 3.2.2
- JVM: JDK 17.0.3, OpenJDK 64-Bit Server VM, 17.0.3+7-LTS
- slinc: 0.1.1-110-7863cb
- Apple clang version 13.1.6 (clang-1316.0.21.2.5)

slinc one takes around 3x~ longer than jni.

Result

| Benchmark              | Mode | Cnt | Score     | Error      | Units |
| ---------------------- | ---- | --- | --------- | ---------- | ----- |
| NativeBenchmarks.jni   | avgt | 5   | 5064.292  | ±  593.829 | ns/op |
| NativeBenchmarks.slinc | avgt | 5   | 16882.792 | ± 1172.054 | ns/op |


However, just updating JDK to Zulu19.30+11-CA, slinc one gets nearly as fast as jni one.

JVM: OpenJDK Runtime Environment Zulu19.30+11-CA (build 19.0.1+10)

| Benchmark              | Mode | Cnt | Score    | Error     | Units |
| ---------------------- | ---- | --- | -------- | --------- | ----- |
| NativeBenchmarks.jni   | avgt | 5   | 4872.056 | ±  57.582 | ns/op |
| NativeBenchmarks.slinc | avgt | 5   | 5607.126 | ± 115.210 | ns/op |

## Caveat

JNI or FFI is not always the fastest solution.

For example, see benchmark for quicksort and you can find `SimpleNativeCallBenchmarks.slincQSortJVM` is the fastest.

You should take overhead into consideration, find real bottleneck and carefully measure the performance benefits before resorting to FFI.




