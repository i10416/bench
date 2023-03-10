/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class nativebench_ctimeJNIBenchHelper */

#ifndef _Included_nativebench_ctimeJNIBenchHelper
#define _Included_nativebench_ctimeJNIBenchHelper
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallDerefInt
 * Signature:  (J)I
 */
JNIEXPORT jint JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallDerefInt
  (JNIEnv *, jobject, jlong);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallDerefLong
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallDerefLong
  (JNIEnv *, jobject, jlong);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallAllocEmptyIntPtr
 * Signature:  ()J
 */
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallAllocEmptyIntPtr
  (JNIEnv *, jobject);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallAllocLongPtr
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallAllocLongPtr
  (JNIEnv *, jobject, jlong);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallAllocString
 * Signature:  (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallAllocString
  (JNIEnv *, jobject, jstring);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallAllocTM
 * Signature:  (Lnativebench/tm;)J
 */
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallAllocTM
  (JNIEnv *, jobject, jobject);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     upcall
 * Signature:  ()V
 */
JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_upcall
  (JNIEnv *, jobject);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     callNativeIntQsort
 * Signature:  ([I)V
 */
JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_callNativeIntQsort
  (JNIEnv *, jobject, jintArray);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     callIntQsort
 * Signature:  ([I)V
 */
JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_callIntQsort
  (JNIEnv *, jobject, jintArray);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallSscanf
 * Signature:  (JJJJJJJ)V
 */
JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallSscanf
  (JNIEnv *, jobject, jlong, jlong, jlong, jlong, jlong, jlong, jlong);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallMktime
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallMktime
  (JNIEnv *, jobject, jlong);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallCtime
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallCtime
  (JNIEnv *, jobject, jlong);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     jniCallCpStr
 * Signature:  (JI)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallCpStr
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:      nativebench_ctimeJNIBenchHelper
 * Method:     setup
 * Signature:  ()V
 */
JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_setup
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
