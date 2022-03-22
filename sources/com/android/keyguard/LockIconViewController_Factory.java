package com.android.keyguard;

import android.content.res.Resources;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LockIconViewController_Factory implements Factory<LockIconViewController> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<AuthController> authControllerProvider;
    public final Provider<AuthRippleController> authRippleControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> executorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<KeyguardViewController> keyguardViewControllerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VibratorHelper> vibratorProvider;
    public final Provider<LockIconView> viewProvider;

    public static LockIconViewController_Factory create(Provider<LockIconView> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<KeyguardViewController> provider4, Provider<KeyguardStateController> provider5, Provider<FalsingManager> provider6, Provider<AuthController> provider7, Provider<DumpManager> provider8, Provider<AccessibilityManager> provider9, Provider<ConfigurationController> provider10, Provider<DelayableExecutor> provider11, Provider<VibratorHelper> provider12, Provider<AuthRippleController> provider13, Provider<Resources> provider14) {
        return new LockIconViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        LockIconView lockIconView = this.viewProvider.mo144get();
        StatusBarStateController statusBarStateController = this.statusBarStateControllerProvider.mo144get();
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.keyguardUpdateMonitorProvider.mo144get();
        KeyguardViewController keyguardViewController = this.keyguardViewControllerProvider.mo144get();
        KeyguardStateController keyguardStateController = this.keyguardStateControllerProvider.mo144get();
        FalsingManager falsingManager = this.falsingManagerProvider.mo144get();
        AuthController authController = this.authControllerProvider.mo144get();
        DumpManager dumpManager = this.dumpManagerProvider.mo144get();
        this.accessibilityManagerProvider.mo144get();
        return new LockIconViewController(lockIconView, statusBarStateController, keyguardUpdateMonitor, keyguardViewController, keyguardStateController, falsingManager, authController, dumpManager, this.configurationControllerProvider.mo144get(), this.executorProvider.mo144get(), this.vibratorProvider.mo144get(), this.authRippleControllerProvider.mo144get(), this.resourcesProvider.mo144get());
    }

    public LockIconViewController_Factory(Provider<LockIconView> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<KeyguardViewController> provider4, Provider<KeyguardStateController> provider5, Provider<FalsingManager> provider6, Provider<AuthController> provider7, Provider<DumpManager> provider8, Provider<AccessibilityManager> provider9, Provider<ConfigurationController> provider10, Provider<DelayableExecutor> provider11, Provider<VibratorHelper> provider12, Provider<AuthRippleController> provider13, Provider<Resources> provider14) {
        this.viewProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.keyguardViewControllerProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.falsingManagerProvider = provider6;
        this.authControllerProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.accessibilityManagerProvider = provider9;
        this.configurationControllerProvider = provider10;
        this.executorProvider = provider11;
        this.vibratorProvider = provider12;
        this.authRippleControllerProvider = provider13;
        this.resourcesProvider = provider14;
    }
}
