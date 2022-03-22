package com.android.systemui.dagger;

import android.app.ActivityManager;
import android.app.IActivityManager;
import dagger.internal.Factory;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIActivityManagerFactory implements Factory<IActivityManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIActivityManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIActivityManagerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        IActivityManager service = ActivityManager.getService();
        Objects.requireNonNull(service, "Cannot return null from a non-@Nullable @Provides method");
        return service;
    }
}
