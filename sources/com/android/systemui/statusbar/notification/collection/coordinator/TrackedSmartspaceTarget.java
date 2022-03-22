package com.android.systemui.statusbar.notification.collection.coordinator;
/* compiled from: SmartspaceDedupingCoordinator.kt */
/* loaded from: classes.dex */
public final class TrackedSmartspaceTarget {
    public long alertExceptionExpires;
    public Runnable cancelTimeoutRunnable;
    public final String key;
    public boolean shouldFilter;

    public TrackedSmartspaceTarget(String str) {
        this.key = str;
    }
}
