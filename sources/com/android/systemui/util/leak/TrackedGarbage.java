package com.android.systemui.util.leak;

import android.os.SystemClock;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class TrackedGarbage {
    public final HashSet<LeakReference> mGarbage = new HashSet<>();
    public final ReferenceQueue<Object> mRefQueue = new ReferenceQueue<>();
    public final TrackedCollections mTrackedCollections;

    public final synchronized int countOldGarbage() {
        int i;
        boolean z;
        while (true) {
            Reference<? extends Object> poll = this.mRefQueue.poll();
            if (poll == null) {
                break;
            }
            this.mGarbage.remove(poll);
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        Iterator<LeakReference> it = this.mGarbage.iterator();
        i = 0;
        while (it.hasNext()) {
            if (it.next().createdUptimeMillis + 60000 < uptimeMillis) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                i++;
            }
        }
        return i;
    }

    /* loaded from: classes.dex */
    public static class LeakReference extends WeakReference<Object> {
        public final Class<?> clazz;
        public final long createdUptimeMillis = SystemClock.uptimeMillis();

        public LeakReference(Object obj, ReferenceQueue<Object> referenceQueue) {
            super(obj, referenceQueue);
            this.clazz = obj.getClass();
        }
    }

    public TrackedGarbage(TrackedCollections trackedCollections) {
        this.mTrackedCollections = trackedCollections;
    }
}
