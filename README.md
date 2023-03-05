## About

slinc vs jni benchmark for the following routine

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



- JVM: OpenJDK Runtime Environment Zulu19.30+11-CA (build 19.0.1+10)

|Benchmark     |          Mode  |Cnt|     Score|     Error|  Units|
|---|---|---|---|---|---|
|NativeBenchmarks.jni|    avgt|    5|  4872.056 |±  57.582|  ns/op|
|NativeBenchmarks.slinc | avgt   | 5 | 5607.126 |± 115.210  |ns/op|
