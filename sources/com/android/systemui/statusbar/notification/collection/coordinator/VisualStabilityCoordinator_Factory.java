package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSource;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VisualStabilityCoordinator_Factory implements Factory<VisualStabilityCoordinator> {
    public final Provider<DelayableExecutor> delayableExecutorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<NotifPanelEventSource> notifPanelEventSourceProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VisualStabilityProvider> visualStabilityProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public static VisualStabilityCoordinator_Factory create(Provider<DelayableExecutor> provider, Provider<DumpManager> provider2, Provider<HeadsUpManager> provider3, Provider<NotifPanelEventSource> provider4, Provider<StatusBarStateController> provider5, Provider<VisualStabilityProvider> provider6, Provider<WakefulnessLifecycle> provider7) {
        return new VisualStabilityCoordinator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new VisualStabilityCoordinator(this.delayableExecutorProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.notifPanelEventSourceProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.visualStabilityProvider.mo144get(), this.wakefulnessLifecycleProvider.mo144get());
    }

    public VisualStabilityCoordinator_Factory(Provider<DelayableExecutor> provider, Provider<DumpManager> provider2, Provider<HeadsUpManager> provider3, Provider<NotifPanelEventSource> provider4, Provider<StatusBarStateController> provider5, Provider<VisualStabilityProvider> provider6, Provider<WakefulnessLifecycle> provider7) {
        this.delayableExecutorProvider = provider;
        this.dumpManagerProvider = provider2;
        this.headsUpManagerProvider = provider3;
        this.notifPanelEventSourceProvider = provider4;
        this.statusBarStateControllerProvider = provider5;
        this.visualStabilityProvider = provider6;
        this.wakefulnessLifecycleProvider = provider7;
    }
}
