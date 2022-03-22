package com.android.systemui.telephony;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class TelephonyCallback_Factory implements Factory<TelephonyCallback> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final TelephonyCallback_Factory INSTANCE = new TelephonyCallback_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TelephonyCallback();
    }
}
