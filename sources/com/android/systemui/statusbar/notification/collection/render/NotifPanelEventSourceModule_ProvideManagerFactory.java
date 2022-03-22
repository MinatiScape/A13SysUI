package com.android.systemui.statusbar.notification.collection.render;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class NotifPanelEventSourceModule_ProvideManagerFactory implements Factory<NotifPanelEventSourceManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final NotifPanelEventSourceModule_ProvideManagerFactory INSTANCE = new NotifPanelEventSourceModule_ProvideManagerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotifPanelEventSourceManagerImpl();
    }
}
