package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class NotifInflationErrorManager_Factory implements Factory<NotifInflationErrorManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final NotifInflationErrorManager_Factory INSTANCE = new NotifInflationErrorManager_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotifInflationErrorManager();
    }
}
