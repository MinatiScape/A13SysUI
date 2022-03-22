package com.android.systemui.dagger;

import android.app.ActivityManager;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class GlobalModule_ProvideIsTestHarnessFactory implements Factory<Boolean> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final GlobalModule_ProvideIsTestHarnessFactory INSTANCE = new GlobalModule_ProvideIsTestHarnessFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return Boolean.valueOf(ActivityManager.isRunningInUserTestHarness());
    }
}
