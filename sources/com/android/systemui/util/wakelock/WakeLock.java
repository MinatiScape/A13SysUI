package com.android.systemui.util.wakelock;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.PowerManager;
import android.util.Log;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import java.util.HashMap;
/* loaded from: classes.dex */
public interface WakeLock {
    static WakeLock wrap(final PowerManager.WakeLock wakeLock, final long j) {
        return new WakeLock() { // from class: com.android.systemui.util.wakelock.WakeLock.1
            public final HashMap<String, Integer> mActiveClients = new HashMap<>();

            @Override // com.android.systemui.util.wakelock.WakeLock
            public final void acquire(String str) {
                this.mActiveClients.putIfAbsent(str, 0);
                HashMap<String, Integer> hashMap = this.mActiveClients;
                hashMap.put(str, Integer.valueOf(hashMap.get(str).intValue() + 1));
                wakeLock.acquire(j);
            }

            @Override // com.android.systemui.util.wakelock.WakeLock
            public final void release(String str) {
                Integer num = this.mActiveClients.get(str);
                if (num == null) {
                    Log.wtf("WakeLock", SupportMenuInflater$$ExternalSyntheticOutline0.m("Releasing WakeLock with invalid reason: ", str), new Throwable());
                    return;
                }
                if (num.intValue() == 1) {
                    this.mActiveClients.remove(str);
                } else {
                    this.mActiveClients.put(str, Integer.valueOf(num.intValue() - 1));
                }
                wakeLock.release();
            }

            public final String toString() {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("active clients= ");
                m.append(this.mActiveClients.toString());
                return m.toString();
            }

            @Override // com.android.systemui.util.wakelock.WakeLock
            public final WakeLock$$ExternalSyntheticLambda0 wrap(Runnable runnable) {
                acquire("wrap");
                return new WakeLock$$ExternalSyntheticLambda0(runnable, this, 0);
            }
        };
    }

    void acquire(String str);

    void release(String str);

    WakeLock$$ExternalSyntheticLambda0 wrap(Runnable runnable);

    /* loaded from: classes.dex */
    public static class Builder {
        public final Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }
    }

    static PowerManager.WakeLock createPartialInner(Context context, String str) {
        return ((PowerManager) context.getSystemService(PowerManager.class)).newWakeLock(1, str);
    }

    static WakeLock createPartial(Context context, String str) {
        return createPartial$1(context, str);
    }

    static WakeLock createPartial$1(Context context, String str) {
        return wrap(createPartialInner(context, str), 20000L);
    }
}
