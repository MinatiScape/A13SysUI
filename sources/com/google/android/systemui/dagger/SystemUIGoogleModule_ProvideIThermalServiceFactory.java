package com.google.android.systemui.dagger;

import android.os.IThermalService;
import android.os.ServiceManager;
import dagger.internal.Factory;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SystemUIGoogleModule_ProvideIThermalServiceFactory implements Factory<IThermalService> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SystemUIGoogleModule_ProvideIThermalServiceFactory INSTANCE = new SystemUIGoogleModule_ProvideIThermalServiceFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        IThermalService asInterface = IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));
        Objects.requireNonNull(asInterface, "Cannot return null from a non-@Nullable @Provides method");
        return asInterface;
    }
}
