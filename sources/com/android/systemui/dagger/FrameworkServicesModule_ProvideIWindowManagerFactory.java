package com.android.systemui.dagger;

import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import dagger.internal.Factory;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIWindowManagerFactory implements Factory<IWindowManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIWindowManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIWindowManagerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
        Objects.requireNonNull(windowManagerService, "Cannot return null from a non-@Nullable @Provides method");
        return windowManagerService;
    }
}
