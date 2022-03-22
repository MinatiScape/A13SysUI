package com.android.systemui.dagger;

import com.android.internal.jank.InteractionJankMonitor;
import dagger.internal.Factory;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideInteractionJankMonitorFactory implements Factory<InteractionJankMonitor> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideInteractionJankMonitorFactory INSTANCE = new FrameworkServicesModule_ProvideInteractionJankMonitorFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        InteractionJankMonitor instance = InteractionJankMonitor.getInstance();
        Objects.requireNonNull(instance, "Cannot return null from a non-@Nullable @Provides method");
        return instance;
    }
}
