package kotlin.jvm.internal;

import java.util.Arrays;
/* loaded from: classes.dex */
public final class Intrinsics {

    /* loaded from: classes.dex */
    public static class Kotlin {
    }

    public static int compare(int i, int i2) {
        if (i < i2) {
            return -1;
        }
        return i == i2 ? 0 : 1;
    }

    public static boolean areEqual(Object obj, Object obj2) {
        if (obj != null) {
            return obj.equals(obj2);
        }
        if (obj2 == null) {
            return true;
        }
        return false;
    }

    public static void checkNotNull(Object obj) {
        if (obj == null) {
            NullPointerException nullPointerException = new NullPointerException();
            sanitizeStackTrace(nullPointerException, Intrinsics.class.getName());
            throw nullPointerException;
        }
    }

    public static String stringPlus(String str, Object obj) {
        return str + obj;
    }

    public static Throwable sanitizeStackTrace(RuntimeException runtimeException, String str) {
        StackTraceElement[] stackTrace = runtimeException.getStackTrace();
        int length = stackTrace.length;
        int i = -1;
        for (int i2 = 0; i2 < length; i2++) {
            if (str.equals(stackTrace[i2].getClassName())) {
                i = i2;
            }
        }
        runtimeException.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(stackTrace, i + 1, length));
        return runtimeException;
    }
}
