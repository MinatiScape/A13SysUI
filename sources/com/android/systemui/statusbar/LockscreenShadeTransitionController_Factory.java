package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LockscreenShadeTransitionController_Factory implements Factory<LockscreenShadeTransitionController> {
    public final Provider<AmbientState> ambientStateProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<NotificationShadeDepthController> depthControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<NotificationLockscreenUserManager> lockScreenUserManagerProvider;
    public final Provider<LSShadeTransitionLogger> loggerProvider;
    public final Provider<MediaHierarchyManager> mediaHierarchyManagerProvider;
    public final Provider<ScrimController> scrimControllerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public static LockscreenShadeTransitionController_Factory create(Provider<SysuiStatusBarStateController> provider, Provider<LSShadeTransitionLogger> provider2, Provider<KeyguardBypassController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<FalsingCollector> provider5, Provider<AmbientState> provider6, Provider<MediaHierarchyManager> provider7, Provider<ScrimController> provider8, Provider<NotificationShadeDepthController> provider9, Provider<Context> provider10, Provider<WakefulnessLifecycle> provider11, Provider<ConfigurationController> provider12, Provider<FalsingManager> provider13, Provider<DumpManager> provider14) {
        return new LockscreenShadeTransitionController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new LockscreenShadeTransitionController(this.statusBarStateControllerProvider.mo144get(), this.loggerProvider.mo144get(), this.keyguardBypassControllerProvider.mo144get(), this.lockScreenUserManagerProvider.mo144get(), this.falsingCollectorProvider.mo144get(), this.ambientStateProvider.mo144get(), this.mediaHierarchyManagerProvider.mo144get(), this.scrimControllerProvider.mo144get(), this.depthControllerProvider.mo144get(), this.contextProvider.mo144get(), this.wakefulnessLifecycleProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public LockscreenShadeTransitionController_Factory(Provider<SysuiStatusBarStateController> provider, Provider<LSShadeTransitionLogger> provider2, Provider<KeyguardBypassController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<FalsingCollector> provider5, Provider<AmbientState> provider6, Provider<MediaHierarchyManager> provider7, Provider<ScrimController> provider8, Provider<NotificationShadeDepthController> provider9, Provider<Context> provider10, Provider<WakefulnessLifecycle> provider11, Provider<ConfigurationController> provider12, Provider<FalsingManager> provider13, Provider<DumpManager> provider14) {
        this.statusBarStateControllerProvider = provider;
        this.loggerProvider = provider2;
        this.keyguardBypassControllerProvider = provider3;
        this.lockScreenUserManagerProvider = provider4;
        this.falsingCollectorProvider = provider5;
        this.ambientStateProvider = provider6;
        this.mediaHierarchyManagerProvider = provider7;
        this.scrimControllerProvider = provider8;
        this.depthControllerProvider = provider9;
        this.contextProvider = provider10;
        this.wakefulnessLifecycleProvider = provider11;
        this.configurationControllerProvider = provider12;
        this.falsingManagerProvider = provider13;
        this.dumpManagerProvider = provider14;
    }
}
