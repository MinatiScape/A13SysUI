package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PulseExpansionHandler_Factory implements Factory<PulseExpansionHandler> {
    public final Provider<KeyguardBypassController> bypassControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    public final Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
    public final Provider<NotificationRoundnessManager> roundnessManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<NotificationWakeUpCoordinator> wakeUpCoordinatorProvider;

    public static PulseExpansionHandler_Factory create(Provider<Context> provider, Provider<NotificationWakeUpCoordinator> provider2, Provider<KeyguardBypassController> provider3, Provider<HeadsUpManagerPhone> provider4, Provider<NotificationRoundnessManager> provider5, Provider<ConfigurationController> provider6, Provider<StatusBarStateController> provider7, Provider<FalsingManager> provider8, Provider<LockscreenShadeTransitionController> provider9, Provider<FalsingCollector> provider10, Provider<DumpManager> provider11) {
        return new PulseExpansionHandler_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PulseExpansionHandler(this.contextProvider.mo144get(), this.wakeUpCoordinatorProvider.mo144get(), this.bypassControllerProvider.mo144get(), this.headsUpManagerProvider.mo144get(), this.roundnessManagerProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.lockscreenShadeTransitionControllerProvider.mo144get(), this.falsingCollectorProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public PulseExpansionHandler_Factory(Provider<Context> provider, Provider<NotificationWakeUpCoordinator> provider2, Provider<KeyguardBypassController> provider3, Provider<HeadsUpManagerPhone> provider4, Provider<NotificationRoundnessManager> provider5, Provider<ConfigurationController> provider6, Provider<StatusBarStateController> provider7, Provider<FalsingManager> provider8, Provider<LockscreenShadeTransitionController> provider9, Provider<FalsingCollector> provider10, Provider<DumpManager> provider11) {
        this.contextProvider = provider;
        this.wakeUpCoordinatorProvider = provider2;
        this.bypassControllerProvider = provider3;
        this.headsUpManagerProvider = provider4;
        this.roundnessManagerProvider = provider5;
        this.configurationControllerProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.falsingManagerProvider = provider8;
        this.lockscreenShadeTransitionControllerProvider = provider9;
        this.falsingCollectorProvider = provider10;
        this.dumpManagerProvider = provider11;
    }
}
