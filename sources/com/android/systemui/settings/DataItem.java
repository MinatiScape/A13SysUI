package com.android.systemui.settings;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.settings.UserTracker;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserTrackerImpl.kt */
/* loaded from: classes.dex */
public final class DataItem {
    public final WeakReference<UserTracker.Callback> callback;
    public final Executor executor;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DataItem)) {
            return false;
        }
        DataItem dataItem = (DataItem) obj;
        return Intrinsics.areEqual(this.callback, dataItem.callback) && Intrinsics.areEqual(this.executor, dataItem.executor);
    }

    public final int hashCode() {
        return this.executor.hashCode() + (this.callback.hashCode() * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DataItem(callback=");
        m.append(this.callback);
        m.append(", executor=");
        m.append(this.executor);
        m.append(')');
        return m.toString();
    }

    public DataItem(WeakReference<UserTracker.Callback> weakReference, Executor executor) {
        this.callback = weakReference;
        this.executor = executor;
    }
}
