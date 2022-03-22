package com.google.android.systemui.dagger;

import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.google.android.systemui.smartspace.BcSmartspaceDataProvider;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SystemUIGoogleModule_ProvideBcSmartspaceDataPluginFactory implements Factory<BcSmartspaceDataPlugin> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final SystemUIGoogleModule_ProvideBcSmartspaceDataPluginFactory INSTANCE = new SystemUIGoogleModule_ProvideBcSmartspaceDataPluginFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new BcSmartspaceDataProvider();
    }
}
