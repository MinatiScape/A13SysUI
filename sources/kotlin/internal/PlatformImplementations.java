package kotlin.internal;

import java.lang.reflect.Method;
import kotlin.random.FallbackThreadLocalRandom;
import kotlin.random.Random;
/* compiled from: PlatformImplementations.kt */
/* loaded from: classes.dex */
public class PlatformImplementations {

    /* compiled from: PlatformImplementations.kt */
    /* loaded from: classes.dex */
    public static final class ReflectThrowable {
        public static final Method addSuppressed;

        /* JADX WARN: Removed duplicated region for block: B:21:0x0030 A[SYNTHETIC] */
        static {
            /*
                java.lang.Class<java.lang.Throwable> r0 = java.lang.Throwable.class
                java.lang.reflect.Method[] r1 = r0.getMethods()
                int r2 = r1.length
                r3 = 0
                r4 = r3
            L_0x0009:
                r5 = 0
                if (r4 >= r2) goto L_0x0031
                r6 = r1[r4]
                int r4 = r4 + 1
                java.lang.String r7 = r6.getName()
                java.lang.String r8 = "addSuppressed"
                boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r7, r8)
                r8 = 1
                if (r7 == 0) goto L_0x002d
                java.lang.Class[] r7 = r6.getParameterTypes()
                int r9 = r7.length
                if (r9 != r8) goto L_0x0026
                r5 = r7[r3]
            L_0x0026:
                boolean r5 = kotlin.jvm.internal.Intrinsics.areEqual(r5, r0)
                if (r5 == 0) goto L_0x002d
                goto L_0x002e
            L_0x002d:
                r8 = r3
            L_0x002e:
                if (r8 == 0) goto L_0x0009
                r5 = r6
            L_0x0031:
                kotlin.internal.PlatformImplementations.ReflectThrowable.addSuppressed = r5
                int r0 = r1.length
            L_0x0034:
                if (r3 >= r0) goto L_0x0046
                r2 = r1[r3]
                int r3 = r3 + 1
                java.lang.String r2 = r2.getName()
                java.lang.String r4 = "getSuppressed"
                boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual(r2, r4)
                if (r2 == 0) goto L_0x0034
            L_0x0046:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlin.internal.PlatformImplementations.ReflectThrowable.<clinit>():void");
        }
    }

    public void addSuppressed(Throwable th, Throwable th2) {
        Method method = ReflectThrowable.addSuppressed;
        if (method != null) {
            method.invoke(th, th2);
        }
    }

    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }
}
