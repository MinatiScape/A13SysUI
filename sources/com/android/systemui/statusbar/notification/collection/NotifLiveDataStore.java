package com.android.systemui.statusbar.notification.collection;
/* compiled from: NotifLiveDataStore.kt */
/* loaded from: classes.dex */
public interface NotifLiveDataStore {
    NotifLiveDataImpl getActiveNotifCount();

    NotifLiveDataImpl getActiveNotifList();

    NotifLiveDataImpl getHasActiveNotifs();
}
