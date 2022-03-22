package com.android.systemui.classifier;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class FalsingModule_ProvidesDoubleTapTimeoutMsFactory implements Factory<Long> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final FalsingModule_ProvidesDoubleTapTimeoutMsFactory INSTANCE = new FalsingModule_ProvidesDoubleTapTimeoutMsFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return 1200L;
    }
}
