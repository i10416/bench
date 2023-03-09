## About


slinc vs jni benchmark


### benchmark for simple operations

#### Qsort benchmark

In general, it is clear that upcall JVM method from native is quite slow.
The following change makes a program extremely slower(check `SimpleNativeCallBenchmarks.jniNativeQSort` and `SimpleNativeCallBenchmarks.jniQSort`). There's only a small difference in performance between array copy back and forth and array copy without copy back. See `SimpleNativeCallBenchmarks.slincQSortWithCopyBack	` and `SimpleNativeCallBenchmarks.slincQSortWithoutCopyBack`.

`SimpleNativeCallBenchmarks.slincQsortAllocCallbackForEachIteration` is much slower than `slincQSortWithCopyBack` and `slincQSortWithoutCopyBack`. Allocating upcall seems cosly operation.


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

SlinC one is around 2 times slower than JNI one when allocating callback in advance and 5x~ times slower when allocating callback for each iteration.

~~SlinC one is around 5 times slower than JNI one probably because SlincQsort transfers array between JVM and native, but I suspect there are other reasons why SlinC ones are slow because there is not large difference between `slincQSortWithCopyBack` and `slincQSortWithoutCopyBack`, which implies data transfer is not the bottleneck~~



| Benchmark                                                          |   NOTE  | Mode | Cnt | Score       | Error        | Units |
| ------------------------------------------------------------------ | --- | ---- | --- | ----------- | ------------ | ----- |
| SimpleNativeCallBenchmarks.jniNativeQSort                          | Using native comparator. __No upcall__     | avgt | 5   | 4272.838    | ±     50.298 | ns/op |
| SimpleNativeCallBenchmarks.jniQSort                                |  Using upcall. destructively mutate original array    | avgt | 5   | 299570.811  | ±   4542.836 | ns/op |
| SimpleNativeCallBenchmarks.slincQSortWithCopyBack                  |  Using global shared upcall. Copy array back and forth.   | avgt | 5   | 618014.439  | ±   8280.107 | ns/op |
| SimpleNativeCallBenchmarks.slincQSortWithoutCopyBack               |Using upcall. Copy and transfer array but not copy back. | avgt | 5   | 625336.580  | ±  10471.754 | ns/op |
| SimpleNativeCallBenchmarks.slincQsortAllocCallbackForEachIteration | Allocating upcall for each iteration.  | avgt | 5   | 1700443.210 | ± 650331.220 | ns/op |

### benchmark for more complex routine
 for the following routine

1. copy string (jvm to native)
2. invoke native call (jvm to native)
3. dereferencing pointer (native to jvm)
4. copy object (jvm to native)
5. copy object (native to jvm)

Environment

- Scala 3.2.2
- JVM: JDK 17.0.3, OpenJDK 64-Bit Server VM, 17.0.3+7-LTS
- slinc: 0.1.1-110-7863cb
- Apple clang version 13.1.6 (clang-1316.0.21.2.5)

Result

| Benchmark              | Mode | Cnt | Score     | Error      | Units |
| ---------------------- | ---- | --- | --------- | ---------- | ----- |
| NativeBenchmarks.jni   | avgt | 5   | 5064.292  | ±  593.829 | ns/op |
| NativeBenchmarks.slinc | avgt | 5   | 16882.792 | ± 1172.054 | ns/op |



JVM: OpenJDK Runtime Environment Zulu19.30+11-CA (build 19.0.1+10)

| Benchmark              | Mode | Cnt | Score    | Error     | Units |
| ---------------------- | ---- | --- | -------- | --------- | ----- |
| NativeBenchmarks.jni   | avgt | 5   | 4872.056 | ±  57.582 | ns/op |
| NativeBenchmarks.slinc | avgt | 5   | 5607.126 | ± 115.210 | ns/op |



