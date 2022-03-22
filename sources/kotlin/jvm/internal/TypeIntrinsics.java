package kotlin.jvm.internal;

import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class TypeIntrinsics {
    /* JADX WARN: Code restructure failed: missing block: B:78:0x00b9, code lost:
        if (r0 == r4) goto L_0x00bd;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.Object beforeCheckcastToFunctionOfArity(java.lang.Object r3, int r4) {
        /*
            Method dump skipped, instructions count: 215
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(java.lang.Object, int):java.lang.Object");
    }

    public static void throwCce(Object obj, String str) {
        String str2;
        if (obj == null) {
            str2 = "null";
        } else {
            str2 = obj.getClass().getName();
        }
        ClassCastException classCastException = new ClassCastException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m(str2, " cannot be cast to ", str));
        Intrinsics.sanitizeStackTrace(classCastException, TypeIntrinsics.class.getName());
        throw classCastException;
    }
}
