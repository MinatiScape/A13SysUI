package com.android.systemui.util;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DeviceConfigProxy_Factory implements Factory<DeviceConfigProxy> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final DeviceConfigProxy_Factory INSTANCE = new DeviceConfigProxy_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DeviceConfigProxy();
    }
}
