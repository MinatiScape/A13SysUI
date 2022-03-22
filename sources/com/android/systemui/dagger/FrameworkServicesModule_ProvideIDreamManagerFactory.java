package com.android.systemui.dagger;

import android.os.ServiceManager;
import android.service.dreams.IDreamManager;
import dagger.internal.Factory;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIDreamManagerFactory implements Factory<IDreamManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIDreamManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIDreamManagerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        IDreamManager asInterface = IDreamManager.Stub.asInterface(ServiceManager.checkService("dreams"));
        Objects.requireNonNull(asInterface, "Cannot return null from a non-@Nullable @Provides method");
        return asInterface;
    }
}
