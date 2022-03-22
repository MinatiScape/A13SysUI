package com.android.systemui.statusbar.notification.collection.coordinator;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class HideLocallyDismissedNotifsCoordinator_Factory implements Factory<HideLocallyDismissedNotifsCoordinator> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final HideLocallyDismissedNotifsCoordinator_Factory INSTANCE = new HideLocallyDismissedNotifsCoordinator_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HideLocallyDismissedNotifsCoordinator();
    }
}
