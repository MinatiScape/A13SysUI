package com.android.systemui.statusbar.notification.collection.render;
/* compiled from: NotifPanelEventSource.kt */
/* loaded from: classes.dex */
public interface NotifPanelEventSource {

    /* compiled from: NotifPanelEventSource.kt */
    /* loaded from: classes.dex */
    public interface Callbacks {
        void onPanelCollapsingChanged(boolean z);
    }

    void registerCallbacks(Callbacks callbacks);

    void unregisterCallbacks(Callbacks callbacks);
}
