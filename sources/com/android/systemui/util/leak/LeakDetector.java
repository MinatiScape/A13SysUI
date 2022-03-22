package com.android.systemui.util.leak;

import android.os.Build;
import android.os.SystemClock;
import android.util.ArrayMap;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.leak.TrackedGarbage;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LeakDetector implements Dumpable {
    public static final boolean ENABLED = Build.IS_DEBUGGABLE;
    public final TrackedCollections mTrackedCollections;
    public final TrackedGarbage mTrackedGarbage;
    public final TrackedObjects mTrackedObjects;

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        boolean z;
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("SYSUI LEAK DETECTOR");
        indentingPrintWriter.increaseIndent();
        if (this.mTrackedCollections == null || this.mTrackedGarbage == null) {
            indentingPrintWriter.println("disabled");
        } else {
            indentingPrintWriter.println("TrackedCollections:");
            indentingPrintWriter.increaseIndent();
            this.mTrackedCollections.dump(indentingPrintWriter, LeakDetector$$ExternalSyntheticLambda0.INSTANCE);
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.println("TrackedObjects:");
            indentingPrintWriter.increaseIndent();
            this.mTrackedCollections.dump(indentingPrintWriter, LeakDetector$$ExternalSyntheticLambda1.INSTANCE);
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.print("TrackedGarbage:");
            indentingPrintWriter.increaseIndent();
            TrackedGarbage trackedGarbage = this.mTrackedGarbage;
            Objects.requireNonNull(trackedGarbage);
            synchronized (trackedGarbage) {
                while (true) {
                    Reference<? extends Object> poll = trackedGarbage.mRefQueue.poll();
                    if (poll == null) {
                        break;
                    }
                    trackedGarbage.mGarbage.remove(poll);
                }
                long uptimeMillis = SystemClock.uptimeMillis();
                ArrayMap arrayMap = new ArrayMap();
                ArrayMap arrayMap2 = new ArrayMap();
                Iterator<TrackedGarbage.LeakReference> it = trackedGarbage.mGarbage.iterator();
                while (it.hasNext()) {
                    TrackedGarbage.LeakReference next = it.next();
                    Class<?> cls = next.clazz;
                    arrayMap.put(cls, Integer.valueOf(((Integer) arrayMap.getOrDefault(cls, 0)).intValue() + 1));
                    if (next.createdUptimeMillis + 60000 < uptimeMillis) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        Class<?> cls2 = next.clazz;
                        arrayMap2.put(cls2, Integer.valueOf(((Integer) arrayMap2.getOrDefault(cls2, 0)).intValue() + 1));
                    }
                }
                for (Map.Entry entry : arrayMap.entrySet()) {
                    indentingPrintWriter.print(((Class) entry.getKey()).getName());
                    indentingPrintWriter.print(": ");
                    indentingPrintWriter.print(entry.getValue());
                    indentingPrintWriter.print(" total, ");
                    indentingPrintWriter.print(arrayMap2.getOrDefault(entry.getKey(), 0));
                    indentingPrintWriter.print(" old");
                    indentingPrintWriter.println();
                }
            }
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
    }

    public final void trackGarbage(Object obj) {
        TrackedGarbage trackedGarbage = this.mTrackedGarbage;
        if (trackedGarbage != null) {
            synchronized (trackedGarbage) {
                while (true) {
                    Reference<? extends Object> poll = trackedGarbage.mRefQueue.poll();
                    if (poll != null) {
                        trackedGarbage.mGarbage.remove(poll);
                    } else {
                        trackedGarbage.mGarbage.add(new TrackedGarbage.LeakReference(obj, trackedGarbage.mRefQueue));
                        trackedGarbage.mTrackedCollections.track(trackedGarbage.mGarbage, "Garbage");
                    }
                }
            }
        }
    }

    @VisibleForTesting
    public LeakDetector(TrackedCollections trackedCollections, TrackedGarbage trackedGarbage, TrackedObjects trackedObjects, DumpManager dumpManager) {
        this.mTrackedCollections = trackedCollections;
        this.mTrackedGarbage = trackedGarbage;
        this.mTrackedObjects = trackedObjects;
        dumpManager.registerDumpable("LeakDetector", this);
    }
}
