package kotlinx.coroutines;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
/* compiled from: CoroutineContext.kt */
/* loaded from: classes.dex */
public final class CoroutineContextKt {
    public static final boolean useCoroutinesScheduler;

    /* JADX WARN: Code restructure failed: missing block: B:16:0x002b, code lost:
        if (r0.equals("on") != false) goto L_0x0057;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0034, code lost:
        if (r0.equals("") != false) goto L_0x0057;
     */
    static {
        /*
            java.lang.String r0 = "kotlinx.coroutines.scheduler"
            int r1 = kotlinx.coroutines.internal.SystemPropsKt__SystemPropsKt.AVAILABLE_PROCESSORS
            java.lang.String r0 = java.lang.System.getProperty(r0)     // Catch: SecurityException -> 0x0009
            goto L_0x000a
        L_0x0009:
            r0 = 0
        L_0x000a:
            if (r0 == 0) goto L_0x0057
            int r1 = r0.hashCode()
            if (r1 == 0) goto L_0x002e
            r2 = 3551(0xddf, float:4.976E-42)
            if (r1 == r2) goto L_0x0025
            r2 = 109935(0x1ad6f, float:1.54052E-40)
            if (r1 != r2) goto L_0x0037
            java.lang.String r1 = "off"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0037
            r0 = 0
            goto L_0x0058
        L_0x0025:
            java.lang.String r1 = "on"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0037
            goto L_0x0057
        L_0x002e:
            java.lang.String r1 = ""
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0037
            goto L_0x0057
        L_0x0037:
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "System property 'kotlinx.coroutines.scheduler' has unrecognized value '"
            r2.append(r3)
            r2.append(r0)
            r0 = 39
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            java.lang.String r0 = r0.toString()
            r1.<init>(r0)
            throw r1
        L_0x0057:
            r0 = 1
        L_0x0058:
            kotlinx.coroutines.CoroutineContextKt.useCoroutinesScheduler = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CoroutineContextKt.<clinit>():void");
    }

    public static final UndispatchedCoroutine<?> updateUndispatchedCompletion(Continuation<?> continuation, CoroutineContext coroutineContext, Object obj) {
        boolean z;
        UndispatchedCoroutine<?> undispatchedCoroutine = null;
        if (!(continuation instanceof CoroutineStackFrame)) {
            return null;
        }
        if (coroutineContext.get(UndispatchedMarker.INSTANCE) != null) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return null;
        }
        CoroutineStackFrame coroutineStackFrame = (CoroutineStackFrame) continuation;
        while (true) {
            coroutineStackFrame = coroutineStackFrame.getCallerFrame();
            if (coroutineStackFrame != null) {
                if (coroutineStackFrame instanceof UndispatchedCoroutine) {
                    undispatchedCoroutine = (UndispatchedCoroutine) coroutineStackFrame;
                    break;
                }
            } else {
                break;
            }
        }
        if (undispatchedCoroutine != null) {
            undispatchedCoroutine.savedContext = coroutineContext;
            undispatchedCoroutine.savedOldValue = obj;
        }
        return undispatchedCoroutine;
    }
}
