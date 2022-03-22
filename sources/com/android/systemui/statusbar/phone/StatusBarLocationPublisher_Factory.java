package com.android.systemui.statusbar.phone;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class StatusBarLocationPublisher_Factory implements Factory<StatusBarLocationPublisher> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final StatusBarLocationPublisher_Factory INSTANCE = new StatusBarLocationPublisher_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new StatusBarLocationPublisher();
    }
}
