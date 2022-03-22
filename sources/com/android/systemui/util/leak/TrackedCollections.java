package com.android.systemui.util.leak;

import android.os.SystemClock;
import com.android.internal.util.IndentingPrintWriter;
import com.android.systemui.util.leak.WeakIdentityHashMap;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class TrackedCollections {
    public final WeakIdentityHashMap<Collection<?>, CollectionState> mCollections = new WeakIdentityHashMap<>();

    /* loaded from: classes.dex */
    public static class CollectionState {
        public int halfwayCount = -1;
        public int lastCount = -1;
        public long startUptime;
        public String tag;

        public static float ratePerHour(long j, int i, long j2, int i2) {
            if (j >= j2 || i < 0 || i2 < 0) {
                return Float.NaN;
            }
            return ((i2 - i) / ((float) (j2 - j))) * 60.0f * 60000.0f;
        }

        public final void dump(IndentingPrintWriter indentingPrintWriter) {
            long uptimeMillis = SystemClock.uptimeMillis();
            long j = this.startUptime;
            indentingPrintWriter.format("%s: %.2f (start-30min) / %.2f (30min-now) / %.2f (start-now) (growth rate in #/hour); %d (current size)", this.tag, Float.valueOf(ratePerHour(j, 0, j + 1800000, this.halfwayCount)), Float.valueOf(ratePerHour(1800000 + this.startUptime, this.halfwayCount, uptimeMillis, this.lastCount)), Float.valueOf(ratePerHour(this.startUptime, 0, uptimeMillis, this.lastCount)), Integer.valueOf(this.lastCount));
        }
    }

    public final synchronized void dump(IndentingPrintWriter indentingPrintWriter, Predicate predicate) {
        WeakIdentityHashMap<Collection<?>, CollectionState> weakIdentityHashMap = this.mCollections;
        Objects.requireNonNull(weakIdentityHashMap);
        for (Map.Entry<WeakReference<Collection<?>>, CollectionState> entry : weakIdentityHashMap.mMap.entrySet()) {
            Collection<?> collection = entry.getKey().get();
            if (collection != null && predicate.test(collection)) {
                entry.getValue().dump(indentingPrintWriter);
                indentingPrintWriter.println();
            }
        }
    }

    public final synchronized void track(Collection<?> collection, String str) {
        WeakIdentityHashMap<Collection<?>, CollectionState> weakIdentityHashMap = this.mCollections;
        Objects.requireNonNull(weakIdentityHashMap);
        weakIdentityHashMap.cleanUp();
        CollectionState collectionState = weakIdentityHashMap.mMap.get(new WeakIdentityHashMap.CmpWeakReference(collection));
        if (collectionState == null) {
            collectionState = new CollectionState();
            collectionState.tag = str;
            collectionState.startUptime = SystemClock.uptimeMillis();
            WeakIdentityHashMap<Collection<?>, CollectionState> weakIdentityHashMap2 = this.mCollections;
            Objects.requireNonNull(weakIdentityHashMap2);
            weakIdentityHashMap2.cleanUp();
            weakIdentityHashMap2.mMap.put(new WeakIdentityHashMap.CmpWeakReference(collection, weakIdentityHashMap2.mRefQueue), collectionState);
        }
        if (collectionState.halfwayCount == -1 && SystemClock.uptimeMillis() - collectionState.startUptime > 1800000) {
            collectionState.halfwayCount = collectionState.lastCount;
        }
        collectionState.lastCount = collection.size();
        SystemClock.uptimeMillis();
    }
}
