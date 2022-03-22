package kotlinx.coroutines;

import android.content.Context;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
/* compiled from: Yield.kt */
/* loaded from: classes.dex */
public final class YieldKt {
    public static boolean shouldInflateSignalStrength(Context context, int i) {
        PersistableBundle persistableBundle;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            persistableBundle = carrierConfigManager.getConfigForSubId(i);
        } else {
            persistableBundle = null;
        }
        if (persistableBundle == null || !persistableBundle.getBoolean("inflate_signal_strength_bool", false)) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0087  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final java.lang.Object yield(kotlin.coroutines.Continuation r8) {
        /*
            kotlin.coroutines.intrinsics.CoroutineSingletons r0 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            kotlin.coroutines.CoroutineContext r1 = r8.getContext()
            kotlinx.coroutines.JobKt.ensureActive(r1)
            kotlin.coroutines.Continuation r8 = androidx.preference.R$color.intercepted(r8)
            boolean r2 = r8 instanceof kotlinx.coroutines.internal.DispatchedContinuation
            r3 = 0
            if (r2 == 0) goto L_0x0015
            kotlinx.coroutines.internal.DispatchedContinuation r8 = (kotlinx.coroutines.internal.DispatchedContinuation) r8
            goto L_0x0016
        L_0x0015:
            r8 = r3
        L_0x0016:
            if (r8 != 0) goto L_0x001c
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            goto L_0x0090
        L_0x001c:
            kotlinx.coroutines.CoroutineDispatcher r2 = r8.dispatcher
            boolean r2 = r2.isDispatchNeeded()
            r4 = 1
            if (r2 == 0) goto L_0x0031
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r8._state = r2
            r8.resumeMode = r4
            kotlinx.coroutines.CoroutineDispatcher r2 = r8.dispatcher
            r2.dispatchYield(r1, r8)
            goto L_0x008f
        L_0x0031:
            kotlinx.coroutines.YieldContext r2 = new kotlinx.coroutines.YieldContext
            r2.<init>()
            kotlin.coroutines.CoroutineContext r1 = r1.plus(r2)
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            r8._state = r5
            r8.resumeMode = r4
            kotlinx.coroutines.CoroutineDispatcher r6 = r8.dispatcher
            r6.dispatchYield(r1, r8)
            boolean r1 = r2.dispatcherWasUnconfined
            if (r1 == 0) goto L_0x008f
            boolean r1 = kotlinx.coroutines.DebugKt.DEBUG
            kotlinx.coroutines.EventLoop r1 = kotlinx.coroutines.ThreadLocalEventLoop.getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines()
            kotlinx.coroutines.internal.ArrayQueue<kotlinx.coroutines.DispatchedTask<?>> r2 = r1.unconfinedQueue
            r6 = 0
            if (r2 != 0) goto L_0x0055
            goto L_0x005b
        L_0x0055:
            int r7 = r2.head
            int r2 = r2.tail
            if (r7 != r2) goto L_0x005d
        L_0x005b:
            r2 = r4
            goto L_0x005e
        L_0x005d:
            r2 = r6
        L_0x005e:
            if (r2 == 0) goto L_0x0061
            goto L_0x0083
        L_0x0061:
            boolean r2 = r1.isUnconfinedLoopActive()
            if (r2 == 0) goto L_0x006f
            r8._state = r5
            r8.resumeMode = r4
            r1.dispatchUnconfined(r8)
            goto L_0x0084
        L_0x006f:
            r1.incrementUseCount(r4)
            r8.run()     // Catch: all -> 0x007c
        L_0x0075:
            boolean r2 = r1.processUnconfinedEvent()     // Catch: all -> 0x007c
            if (r2 != 0) goto L_0x0075
            goto L_0x0080
        L_0x007c:
            r2 = move-exception
            r8.handleFatalException(r2, r3)     // Catch: all -> 0x008a
        L_0x0080:
            r1.decrementUseCount(r4)
        L_0x0083:
            r4 = r6
        L_0x0084:
            if (r4 == 0) goto L_0x0087
            goto L_0x008f
        L_0x0087:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            goto L_0x0090
        L_0x008a:
            r8 = move-exception
            r1.decrementUseCount(r4)
            throw r8
        L_0x008f:
            r8 = r0
        L_0x0090:
            if (r8 != r0) goto L_0x0093
            return r8
        L_0x0093:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.YieldKt.yield(kotlin.coroutines.Continuation):java.lang.Object");
    }
}
