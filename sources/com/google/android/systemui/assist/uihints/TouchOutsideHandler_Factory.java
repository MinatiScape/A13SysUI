package com.google.android.systemui.assist.uihints;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class TouchOutsideHandler_Factory implements Factory<TouchOutsideHandler> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final TouchOutsideHandler_Factory INSTANCE = new TouchOutsideHandler_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TouchOutsideHandler();
    }
}
