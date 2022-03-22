package kotlinx.coroutines.internal;

import kotlin.Result;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DebugKt;
/* compiled from: StackTraceRecovery.kt */
/* loaded from: classes.dex */
public final class StackTraceRecoveryKt {
    public static final String baseContinuationImplClassName;
    public static final String stackTraceRecoveryClassName;

    public static final int frameIndex(StackTraceElement[] stackTraceElementArr, String str) {
        int length = stackTraceElementArr.length;
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            if (Intrinsics.areEqual(str, stackTraceElementArr[i].getClassName())) {
                return i;
            }
            i = i2;
        }
        return -1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [kotlin.Result$Failure] */
    static {
        String str;
        Object obj;
        String str2 = "kotlin.coroutines.jvm.internal.BaseContinuationImpl";
        try {
            str = Class.forName(str2).getCanonicalName();
        } catch (Throwable th) {
            str = new Result.Failure(th);
        }
        if (Result.m181exceptionOrNullimpl(str) == null) {
            str2 = str;
        }
        baseContinuationImplClassName = str2;
        try {
            obj = StackTraceRecoveryKt.class.getCanonicalName();
        } catch (Throwable th2) {
            obj = new Result.Failure(th2);
        }
        if (Result.m181exceptionOrNullimpl(obj) != null) {
            obj = "kotlinx.coroutines.internal.StackTraceRecoveryKt";
        }
        stackTraceRecoveryClassName = (String) obj;
    }

    public static final <E extends Throwable> E recoverStackTrace(E e) {
        int i;
        StackTraceElement stackTraceElement;
        if (!DebugKt.RECOVER_STACK_TRACES) {
            return e;
        }
        E e2 = (E) tryCopyAndVerify(e);
        if (e2 == null) {
            return e;
        }
        StackTraceElement[] stackTrace = e2.getStackTrace();
        int length = stackTrace.length;
        int frameIndex = frameIndex(stackTrace, stackTraceRecoveryClassName);
        int i2 = frameIndex + 1;
        int frameIndex2 = frameIndex(stackTrace, baseContinuationImplClassName);
        if (frameIndex2 == -1) {
            i = 0;
        } else {
            i = length - frameIndex2;
        }
        int i3 = (length - frameIndex) - i;
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            if (i4 == 0) {
                stackTraceElement = new StackTraceElement(Intrinsics.stringPlus("\b\b\b(", "Coroutine boundary"), "\b", "\b", -1);
            } else {
                stackTraceElement = stackTrace[(i2 + i4) - 1];
            }
            stackTraceElementArr[i4] = stackTraceElement;
        }
        e2.setStackTrace(stackTraceElementArr);
        return e2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:111:0x010f A[EDGE_INSN: B:111:0x010f->B:65:0x010f ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <E extends java.lang.Throwable> E tryCopyAndVerify(E r10) {
        /*
            Method dump skipped, instructions count: 387
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.StackTraceRecoveryKt.tryCopyAndVerify(java.lang.Throwable):java.lang.Throwable");
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:80:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final java.lang.Throwable access$recoverFromStackFrame(java.lang.Throwable r12, kotlin.coroutines.jvm.internal.CoroutineStackFrame r13) {
        /*
            Method dump skipped, instructions count: 338
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.StackTraceRecoveryKt.access$recoverFromStackFrame(java.lang.Throwable, kotlin.coroutines.jvm.internal.CoroutineStackFrame):java.lang.Throwable");
    }

    public static final <E extends Throwable> E unwrapImpl(E e) {
        E e2 = (E) e.getCause();
        if (e2 != null && Intrinsics.areEqual(e2.getClass(), e.getClass())) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            int length = stackTrace.length;
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                StackTraceElement stackTraceElement = stackTrace[i];
                i++;
                if (stackTraceElement.getClassName().startsWith("\b\b\b")) {
                    z = true;
                    break;
                }
            }
            if (z) {
                return e2;
            }
        }
        return e;
    }
}
