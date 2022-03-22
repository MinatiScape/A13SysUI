package kotlinx.coroutines;

import java.util.concurrent.atomic.AtomicLong;
/* compiled from: Debug.kt */
/* loaded from: classes.dex */
public final class DebugKt {
    public static final AtomicLong COROUTINE_ID;
    public static final boolean DEBUG;
    public static final boolean RECOVER_STACK_TRACES;

    /* JADX WARN: Code restructure failed: missing block: B:20:0x003b, code lost:
        if (r0.equals("on") != false) goto L_0x0046;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0044, code lost:
        if (r0.equals("") != false) goto L_0x0046;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0046, code lost:
        r0 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006d  */
    static {
        /*
            java.lang.String r0 = "kotlinx.coroutines.debug"
            int r1 = kotlinx.coroutines.internal.SystemPropsKt__SystemPropsKt.AVAILABLE_PROCESSORS
            r1 = 0
            java.lang.String r0 = java.lang.System.getProperty(r0)     // Catch: SecurityException -> 0x000a
            goto L_0x000b
        L_0x000a:
            r0 = r1
        L_0x000b:
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x0068
            int r4 = r0.hashCode()
            if (r4 == 0) goto L_0x003e
            r5 = 3551(0xddf, float:4.976E-42)
            if (r4 == r5) goto L_0x0035
            r5 = 109935(0x1ad6f, float:1.54052E-40)
            if (r4 == r5) goto L_0x002c
            r5 = 3005871(0x2dddaf, float:4.212122E-39)
            if (r4 != r5) goto L_0x0048
            java.lang.String r4 = "auto"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0048
            goto L_0x0068
        L_0x002c:
            java.lang.String r4 = "off"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0048
            goto L_0x0068
        L_0x0035:
            java.lang.String r4 = "on"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0048
            goto L_0x0046
        L_0x003e:
            java.lang.String r4 = ""
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0048
        L_0x0046:
            r0 = r3
            goto L_0x0069
        L_0x0048:
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "System property 'kotlinx.coroutines.debug' has unrecognized value '"
            r2.append(r3)
            r2.append(r0)
            r0 = 39
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            java.lang.String r0 = r0.toString()
            r1.<init>(r0)
            throw r1
        L_0x0068:
            r0 = r2
        L_0x0069:
            kotlinx.coroutines.DebugKt.DEBUG = r0
            if (r0 == 0) goto L_0x0080
            java.lang.String r0 = "kotlinx.coroutines.stacktrace.recovery"
            int r4 = kotlinx.coroutines.internal.SystemPropsKt__SystemPropsKt.AVAILABLE_PROCESSORS
            java.lang.String r1 = java.lang.System.getProperty(r0)     // Catch: SecurityException -> 0x0075
        L_0x0075:
            if (r1 != 0) goto L_0x0079
            r0 = r3
            goto L_0x007d
        L_0x0079:
            boolean r0 = java.lang.Boolean.parseBoolean(r1)
        L_0x007d:
            if (r0 == 0) goto L_0x0080
            r2 = r3
        L_0x0080:
            kotlinx.coroutines.DebugKt.RECOVER_STACK_TRACES = r2
            java.util.concurrent.atomic.AtomicLong r0 = new java.util.concurrent.atomic.AtomicLong
            r1 = 0
            r0.<init>(r1)
            kotlinx.coroutines.DebugKt.COROUTINE_ID = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.DebugKt.<clinit>():void");
    }
}
