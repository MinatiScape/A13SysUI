package kotlinx.coroutines;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CommonPool.kt */
/* loaded from: classes.dex */
public final class CommonPool extends ExecutorCoroutineDispatcher {
    public static final CommonPool INSTANCE = new CommonPool();
    public static volatile ExecutorService pool;
    public static final int requestedParallelism;

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final String toString() {
        return "CommonPool";
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x007c, code lost:
        if (r0.intValue() < 1) goto L_0x0085;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x007e, code lost:
        r0 = r0.intValue();
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x006a A[SYNTHETIC] */
    static {
        /*
            kotlinx.coroutines.CommonPool r0 = new kotlinx.coroutines.CommonPool
            r0.<init>()
            kotlinx.coroutines.CommonPool.INSTANCE = r0
            r0 = 0
            java.lang.String r1 = "kotlinx.coroutines.default.parallelism"
            java.lang.String r1 = java.lang.System.getProperty(r1)     // Catch: all -> 0x000f
            goto L_0x0010
        L_0x000f:
            r1 = r0
        L_0x0010:
            if (r1 != 0) goto L_0x0015
            r0 = -1
            goto L_0x0082
        L_0x0015:
            int r2 = r1.length()
            r3 = 1
            if (r2 != 0) goto L_0x001e
            goto L_0x0076
        L_0x001e:
            r4 = 0
            char r5 = r1.charAt(r4)
            r6 = 48
            int r6 = kotlin.jvm.internal.Intrinsics.compare(r5, r6)
            r7 = -2147483647(0xffffffff80000001, float:-1.4E-45)
            if (r6 >= 0) goto L_0x0040
            if (r2 != r3) goto L_0x0031
            goto L_0x0076
        L_0x0031:
            r6 = 45
            if (r5 != r6) goto L_0x0039
            r7 = -2147483648(0xffffffff80000000, float:-0.0)
            r5 = r3
            goto L_0x0041
        L_0x0039:
            r6 = 43
            if (r5 != r6) goto L_0x0076
            r5 = r3
            r6 = r4
            goto L_0x0042
        L_0x0040:
            r5 = r4
        L_0x0041:
            r6 = r5
        L_0x0042:
            r8 = -59652323(0xfffffffffc71c71d, float:-5.0215282E36)
            r9 = r8
        L_0x0046:
            if (r5 >= r2) goto L_0x006a
            int r10 = r5 + 1
            char r5 = r1.charAt(r5)
            r11 = 10
            int r5 = java.lang.Character.digit(r5, r11)
            if (r5 >= 0) goto L_0x0057
            goto L_0x0076
        L_0x0057:
            if (r4 >= r9) goto L_0x0060
            if (r9 != r8) goto L_0x0076
            int r9 = r7 / 10
            if (r4 >= r9) goto L_0x0060
            goto L_0x0076
        L_0x0060:
            int r4 = r4 * 10
            int r11 = r7 + r5
            if (r4 >= r11) goto L_0x0067
            goto L_0x0076
        L_0x0067:
            int r4 = r4 - r5
            r5 = r10
            goto L_0x0046
        L_0x006a:
            if (r6 == 0) goto L_0x0071
            java.lang.Integer r0 = java.lang.Integer.valueOf(r4)
            goto L_0x0076
        L_0x0071:
            int r0 = -r4
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
        L_0x0076:
            if (r0 == 0) goto L_0x0085
            int r2 = r0.intValue()
            if (r2 < r3) goto L_0x0085
            int r0 = r0.intValue()
        L_0x0082:
            kotlinx.coroutines.CommonPool.requestedParallelism = r0
            return
        L_0x0085:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r2 = "Expected positive number in kotlinx.coroutines.default.parallelism, but has "
            java.lang.String r1 = kotlin.jvm.internal.Intrinsics.stringPlus(r2, r1)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CommonPool.<clinit>():void");
    }

    public static ExecutorService createPlainPool() {
        final AtomicInteger atomicInteger = new AtomicInteger();
        return Executors.newFixedThreadPool(getParallelism(), new ThreadFactory() { // from class: kotlinx.coroutines.CommonPool$createPlainPool$1
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, Intrinsics.stringPlus("CommonPool-worker-", Integer.valueOf(atomicInteger.incrementAndGet())));
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public static int getParallelism() {
        boolean z;
        Integer valueOf = Integer.valueOf(requestedParallelism);
        if (valueOf.intValue() > 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            valueOf = null;
        }
        if (valueOf != null) {
            return valueOf.intValue();
        }
        int availableProcessors = Runtime.getRuntime().availableProcessors() - 1;
        if (availableProcessors < 1) {
            return 1;
        }
        return availableProcessors;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        throw new IllegalStateException("Close cannot be invoked on CommonPool".toString());
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        try {
            ExecutorService executorService = pool;
            if (executorService == null) {
                synchronized (this) {
                    executorService = pool;
                    if (executorService == null) {
                        executorService = createPool();
                        pool = executorService;
                    }
                }
            }
            executorService.execute(runnable);
        } catch (RejectedExecutionException unused) {
            DefaultExecutor.INSTANCE.enqueue(runnable);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x006d A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.concurrent.ExecutorService createPool() {
        /*
            java.lang.SecurityManager r0 = java.lang.System.getSecurityManager()
            if (r0 == 0) goto L_0x000b
            java.util.concurrent.ExecutorService r0 = createPlainPool()
            return r0
        L_0x000b:
            r0 = 0
            java.lang.String r1 = "java.util.concurrent.ForkJoinPool"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch: all -> 0x0013
            goto L_0x0014
        L_0x0013:
            r1 = r0
        L_0x0014:
            if (r1 != 0) goto L_0x001b
            java.util.concurrent.ExecutorService r0 = createPlainPool()
            return r0
        L_0x001b:
            int r2 = kotlinx.coroutines.CommonPool.requestedParallelism
            r3 = 1
            r4 = 0
            if (r2 >= 0) goto L_0x006e
            java.lang.String r2 = "commonPool"
            java.lang.Class[] r5 = new java.lang.Class[r4]     // Catch: all -> 0x0036
            java.lang.reflect.Method r2 = r1.getMethod(r2, r5)     // Catch: all -> 0x0036
            java.lang.Object[] r5 = new java.lang.Object[r4]     // Catch: all -> 0x0036
            java.lang.Object r2 = r2.invoke(r0, r5)     // Catch: all -> 0x0036
            boolean r5 = r2 instanceof java.util.concurrent.ExecutorService     // Catch: all -> 0x0036
            if (r5 == 0) goto L_0x0036
            java.util.concurrent.ExecutorService r2 = (java.util.concurrent.ExecutorService) r2     // Catch: all -> 0x0036
            goto L_0x0037
        L_0x0036:
            r2 = r0
        L_0x0037:
            if (r2 != 0) goto L_0x003a
            goto L_0x006e
        L_0x003a:
            kotlinx.coroutines.CommonPool r5 = kotlinx.coroutines.CommonPool.INSTANCE
            java.util.Objects.requireNonNull(r5)
            kotlinx.coroutines.CommonPool$isGoodCommonPool$1 r5 = kotlinx.coroutines.CommonPool$isGoodCommonPool$1.INSTANCE
            r2.submit(r5)
            java.lang.String r5 = "getPoolSize"
            java.lang.Class[] r6 = new java.lang.Class[r4]     // Catch: all -> 0x0059
            java.lang.reflect.Method r5 = r1.getMethod(r5, r6)     // Catch: all -> 0x0059
            java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch: all -> 0x0059
            java.lang.Object r5 = r5.invoke(r2, r6)     // Catch: all -> 0x0059
            boolean r6 = r5 instanceof java.lang.Integer     // Catch: all -> 0x0059
            if (r6 == 0) goto L_0x0059
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch: all -> 0x0059
            goto L_0x005a
        L_0x0059:
            r5 = r0
        L_0x005a:
            if (r5 != 0) goto L_0x005d
            goto L_0x0065
        L_0x005d:
            int r5 = r5.intValue()
            if (r5 < r3) goto L_0x0065
            r5 = r3
            goto L_0x0066
        L_0x0065:
            r5 = r4
        L_0x0066:
            if (r5 == 0) goto L_0x0069
            goto L_0x006a
        L_0x0069:
            r2 = r0
        L_0x006a:
            if (r2 != 0) goto L_0x006d
            goto L_0x006e
        L_0x006d:
            return r2
        L_0x006e:
            java.lang.Class[] r2 = new java.lang.Class[r3]     // Catch: all -> 0x0094
            java.lang.Class r5 = java.lang.Integer.TYPE     // Catch: all -> 0x0094
            r2[r4] = r5     // Catch: all -> 0x0094
            java.lang.reflect.Constructor r1 = r1.getConstructor(r2)     // Catch: all -> 0x0094
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch: all -> 0x0094
            kotlinx.coroutines.CommonPool r3 = kotlinx.coroutines.CommonPool.INSTANCE     // Catch: all -> 0x0094
            java.util.Objects.requireNonNull(r3)     // Catch: all -> 0x0094
            int r3 = getParallelism()     // Catch: all -> 0x0094
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch: all -> 0x0094
            r2[r4] = r3     // Catch: all -> 0x0094
            java.lang.Object r1 = r1.newInstance(r2)     // Catch: all -> 0x0094
            boolean r2 = r1 instanceof java.util.concurrent.ExecutorService     // Catch: all -> 0x0094
            if (r2 == 0) goto L_0x0094
            java.util.concurrent.ExecutorService r1 = (java.util.concurrent.ExecutorService) r1     // Catch: all -> 0x0094
            r0 = r1
        L_0x0094:
            if (r0 != 0) goto L_0x009a
            java.util.concurrent.ExecutorService r0 = createPlainPool()
        L_0x009a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CommonPool.createPool():java.util.concurrent.ExecutorService");
    }
}
