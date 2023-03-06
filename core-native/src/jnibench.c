#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>

#include "include/nativebench_ctimeJNIBenchHelper.h"

JNIEXPORT jint JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallDerefInt
  (JNIEnv *jenv, jobject jobj, jlong jarg) {
    int* unsafeintptr;
     unsafeintptr =  (int *) jarg;
    return *unsafeintptr;
  }
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallAllocEmptyIntPtr
  (JNIEnv *jenv, jobject jobj) {
    void *mem = malloc(sizeof(int));
    int* unsafeintptr = (int *) mem;
    return (jlong) unsafeintptr;
}
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallAllocLongPtr
  (JNIEnv *jenv, jobject jobj, jlong jarg) {
    long arg = (long) jarg;
    void *mem = malloc(sizeof(long));
    long* unsafelongptr = (long *) mem;
    *unsafelongptr = arg;
    return (jlong) unsafelongptr;
}
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallDerefLong
  (JNIEnv *jenv, jobject jobj, jlong jarg) {
    long * unsafelongptr;
    unsafelongptr = (long *) jarg;
    return *unsafelongptr;
}

JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallAllocString
  (JNIEnv *jenv, jobject jobj, jstring jarg) {
    const char *srcstr = (*jenv)->GetStringUTFChars(jenv,jarg, NULL);
    int len = strlen(srcstr);
    void *mem = malloc(sizeof(char) * len + 1);
    strncpy(mem,srcstr,len);
    return (long) mem;
  };

JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallSscanf
  (JNIEnv *jenv, jobject jobj, jlong srccharptraslong, jlong fmtcharptraslong, jlong yearintptraslong, jlong monthintptraslong, jlong dayintptraslong, jlong hourintptraslong, jlong minintptraslong){
  char * unsafe_src_char_ptr_from_long;
  char * unsafe_fmt_char_ptr_from_long;
  unsafe_src_char_ptr_from_long = (char *) srccharptraslong;
  unsafe_fmt_char_ptr_from_long = (char *) fmtcharptraslong;

  int * unsafe_year_ptr_from_long;
  int * unsafe_month_ptr_from_long;
  int * unsafe_day_ptr_from_long;
  int * unsafe_hour_ptr_from_long;
  int * unsafe_min_ptr_from_long;
  unsafe_year_ptr_from_long = (int *) yearintptraslong;
  unsafe_month_ptr_from_long = (int *) monthintptraslong;
  unsafe_day_ptr_from_long = (int *) dayintptraslong;
  unsafe_hour_ptr_from_long = (int *) hourintptraslong;
  unsafe_min_ptr_from_long = (int *) minintptraslong;
  sscanf(
    unsafe_src_char_ptr_from_long,
    unsafe_fmt_char_ptr_from_long,
    unsafe_year_ptr_from_long,
    unsafe_month_ptr_from_long,
    unsafe_day_ptr_from_long,
    unsafe_hour_ptr_from_long,
    unsafe_min_ptr_from_long
  );
  return;
};
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallAllocTM
  (JNIEnv *jenv, jobject jobj, jobject jarg) {
    jclass clazz = (*jenv)->GetObjectClass(jenv, jarg);
    jfieldID jtm_sec  = (*jenv)->GetFieldID(jenv, clazz, "tm_sec", "I");
    jfieldID jtm_min  = (*jenv)->GetFieldID(jenv, clazz, "tm_min", "I");
    jfieldID jtm_hour  = (*jenv)->GetFieldID(jenv, clazz, "tm_hour", "I");
    jfieldID jtm_mday  = (*jenv)->GetFieldID(jenv, clazz, "tm_mday", "I");
    jfieldID jtm_mon  = (*jenv)->GetFieldID(jenv, clazz, "tm_mon", "I");
    jfieldID jtm_year  = (*jenv)->GetFieldID(jenv, clazz, "tm_year", "I");
    jfieldID jtm_wday  = (*jenv)->GetFieldID(jenv, clazz, "tm_wday", "I");
    jfieldID jtm_yday  = (*jenv)->GetFieldID(jenv, clazz, "tm_yday", "I");
    jfieldID jtm_isdst  = (*jenv)->GetFieldID(jenv, clazz, "tm_isdst", "I");
    int tm_sec = (*jenv)->GetIntField(jenv, jarg, jtm_sec);
    int tm_min = (*jenv)->GetIntField(jenv, jarg, jtm_min);
    int tm_hour = (*jenv)->GetIntField(jenv, jarg, jtm_hour);
    int tm_mday = (*jenv)->GetIntField(jenv, jarg, jtm_mday);
    int tm_mon = (*jenv)->GetIntField(jenv, jarg, jtm_mon);
    int tm_year = (*jenv)->GetIntField(jenv, jarg, jtm_year);
    int tm_wday = (*jenv)->GetIntField(jenv, jarg, jtm_wday);
    int tm_yday = (*jenv)->GetIntField(jenv, jarg, jtm_yday);
    int tm_isdst = (*jenv)->GetIntField(jenv, jarg, jtm_isdst);
    struct tm *tmptr;
    tmptr = (struct tm*) malloc(sizeof(struct tm));
    tmptr->tm_sec = tm_sec;
    tmptr->tm_min = tm_min;
    tmptr->tm_hour = tm_hour;
    tmptr->tm_mday = tm_mday;
    tmptr->tm_mon = tm_mon;
    tmptr->tm_year = tm_year;
    tmptr->tm_wday = tm_wday;
    tmptr->tm_yday = tm_yday;
    tmptr->tm_isdst = tm_isdst;
    return (long) tmptr;
  };
JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallMktime
  (JNIEnv *jenv, jobject jobj, jlong jarg){
    struct tm *unsafetmptr;
    unsafetmptr = (struct tm*) jarg;
    time_t t = mktime(unsafetmptr);
    return (long) t;
};

JNIEXPORT jlong JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallCtime
  (JNIEnv *jenv, jobject jobj, jlong jt) {
    time_t * t;
    t = (time_t *) jt;
    char * ct;
    ct = ctime(t);
    return (long) ct;
  }; 

JNIEXPORT jstring JNICALL Java_nativebench_ctimeJNIBenchHelper_jniCallCpStr
  (JNIEnv *jenv, jobject jobj, jlong char_buf_ptr, jint jlen) {
    char * unsafe_src_char_ptr_from_long;
    unsafe_src_char_ptr_from_long = (char *) char_buf_ptr;
    int len = (int) jlen;
    char *buf = (char*) malloc(len);
    strncpy(buf,unsafe_src_char_ptr_from_long,len);
    jstring jstr = (*jenv)->NewStringUTF(jenv, buf);
    return jstr;
  };

int compare_int(const void *a, const void *b)
{
    return *(int*)a - *(int*)b;
};
static JNIEnv* global_jenv;
static jmethodID global_m;
static jobject caller;

int upcall_compare_int(const void* i,const void* j) {
    return (*global_jenv)->CallIntMethod(global_jenv,caller,global_m,i,j);
};
JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_setup
  (JNIEnv *jenv, jobject jobj) {
    global_jenv = jenv;
    jclass clazz = (*jenv)->FindClass(jenv,"Lnativebench/ctimeJNIBenchHelper;");
    global_m = (*jenv)->GetMethodID(jenv,clazz, "upcallIntCompare", "(JJ)I");
    return;
};

JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_upcall
  (JNIEnv *jenv, jobject jobj) {
    jclass clazz = (*jenv)->FindClass(jenv,"Lnativebench/ctimeJNIBenchHelper;");
    jmethodID m = (*jenv)->GetMethodID(jenv,clazz, "upcallIntCompare", "(JJ)I");
    int i = 2;
    int j = 1;
    int * iptr = &i;
    int * jptr = &j;
    int k = (*jenv)->CallIntMethod(jenv,jobj, m,(long)iptr,(long)jptr);
    return;
  };


JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_callNativeIntQsort
(JNIEnv *jenv, jobject jobj, jintArray jarr){
  jint *arr = (*jenv)->GetIntArrayElements(jenv,jarr, 0);
  jsize len = (*jenv)->GetArrayLength(jenv,jarr);
  qsort(arr,len,sizeof(int),compare_int);
  (*jenv)->ReleaseIntArrayElements(jenv,jarr,arr, 0);
  return;
};

JNIEXPORT void JNICALL Java_nativebench_ctimeJNIBenchHelper_callIntQsort
(JNIEnv *jenv, jobject jobj, jintArray jarr){
  jint *arr = (*jenv)->GetIntArrayElements(jenv,jarr, 0);
  jsize len = (*jenv)->GetArrayLength(jenv,jarr);
  caller = jobj;
  qsort(arr,len,sizeof(int),upcall_compare_int);
  (*jenv)->ReleaseIntArrayElements(jenv,jarr,arr, 0);
  return;
};



