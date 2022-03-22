package com.android.systemui.dagger;

import android.content.pm.IPackageManager;
import android.os.ServiceManager;
import dagger.internal.Factory;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIPackageManagerFactory implements Factory<IPackageManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIPackageManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIPackageManagerFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        IPackageManager asInterface = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        Objects.requireNonNull(asInterface, "Cannot return null from a non-@Nullable @Provides method");
        return asInterface;
    }
}
