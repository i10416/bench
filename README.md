## About


slinc vs jni benchmark


### benchmark for simple operations

#### Qsort benchmark

In general, it is clear that upcall JVM method from native is quite slow.
The following change makes a program extremely slower(check `SimpleNativeCallBenchmarks.jniNativeQSort` and `SimpleNativeCallBenchmarks.jniQSort`).

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

SlinC one is around 5 times slower than JNI one probably because SlincQsort transfers array between JVM and native, but I suspect there are other reasons why SlinC ones are slow because there is not large difference between `slincQSortWithCopyBack` and `slincQSortWithoutCopyBack`, which implies data transfer is not the bottleneck.

Feedback from SlinC author(@markehammons)

> Having cloned your bench and having the callback allocated once (rather than per benchmark iteration), I see a improvement in performance of Slinc's upcall code to just 2x slower than JNI, rather than 5x slower
>
> https://github.com/markehammons/slinc/issues/81#issuecomment-1457928744

| Benchmark                                            || Mode | Cnt | Score       | Error        |Units |
| ---------------------------------------------------- | ---|---- | --- | ----------- | ------------ | ----- |
| SimpleNativeCallBenchmarks.jniNativeQSort            |using native comparator|avgt | 5   | 4113.280    | ±    184.594 | ns/op |
| SimpleNativeCallBenchmarks.jniQSort                  |using upcall comparator, destructively mutate original array|avgt | 5   | 281968.369  | ±   4070.398 | ns/op |
| SimpleNativeCallBenchmarks.slincQSortWithCopyBack    |using upcall comparator, copy and transfer array| avgt | 5   | 1609949.152 | ± 429499.499 | ns/op |
| SimpleNativeCallBenchmarks.slincQSortWithoutCopyBack | using upcall comparator, copy and transfer array, discarding result|avgt | 5   | 1574451.526 | ± 378398.468 | ns/op |

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

|Benchmark     |          Mode  |Cnt|     Score|     Error|  Units|
|---|---|---|---|---|---|
|NativeBenchmarks.jni|    avgt|    5|  4872.056 |±  57.582|  ns/op|
|NativeBenchmarks.slinc | avgt   | 5 | 5607.126 |± 115.210  |ns/op|



