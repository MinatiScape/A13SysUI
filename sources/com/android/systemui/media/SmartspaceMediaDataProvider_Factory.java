package com.android.systemui.media;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SmartspaceMediaDataProvider_Factory implements Factory<SmartspaceMediaDataProvider> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SmartspaceMediaDataProvider_Factory INSTANCE = new SmartspaceMediaDataProvider_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SmartspaceMediaDataProvider();
    }
}
