package com.android.systemui.globalactions;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Handler;
import android.os.UserManager;
import android.service.dreams.IDreamManager;
import android.telecom.TelecomManager;
import android.view.IWindowManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.DelegateFactory;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GlobalActionsDialogLite_Factory implements Factory<GlobalActionsDialogLite> {
    public final Provider<AudioManager> audioManagerProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<SysuiColorExtractor> colorExtractorProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<SystemUIDialogManager> dialogManagerProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<IActivityManager> iActivityManagerProvider;
    public final Provider<IDreamManager> iDreamManagerProvider;
    public final Provider<IWindowManager> iWindowManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<RingerModeTracker> ringerModeTrackerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<Optional<StatusBar>> statusBarOptionalProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;
    public final Provider<SysUiState> sysUiStateProvider;
    public final Provider<TelecomManager> telecomManagerProvider;
    public final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    public final Provider<TrustManager> trustManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<VibratorHelper> vibratorProvider;
    public final Provider<GlobalActions.GlobalActionsManager> windowManagerFuncsProvider;

    public GlobalActionsDialogLite_Factory(Provider provider, DelegateFactory delegateFactory, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, Provider provider18, Provider provider19, Provider provider20, Provider provider21, Provider provider22, Provider provider23, Provider provider24, Provider provider25, Provider provider26, Provider provider27, Provider provider28, Provider provider29, Provider provider30, Provider provider31, Provider provider32) {
        this.contextProvider = provider;
        this.windowManagerFuncsProvider = delegateFactory;
        this.audioManagerProvider = provider2;
        this.iDreamManagerProvider = provider3;
        this.devicePolicyManagerProvider = provider4;
        this.lockPatternUtilsProvider = provider5;
        this.broadcastDispatcherProvider = provider6;
        this.telephonyListenerManagerProvider = provider7;
        this.globalSettingsProvider = provider8;
        this.secureSettingsProvider = provider9;
        this.vibratorProvider = provider10;
        this.resourcesProvider = provider11;
        this.configurationControllerProvider = provider12;
        this.keyguardStateControllerProvider = provider13;
        this.userManagerProvider = provider14;
        this.trustManagerProvider = provider15;
        this.iActivityManagerProvider = provider16;
        this.telecomManagerProvider = provider17;
        this.metricsLoggerProvider = provider18;
        this.colorExtractorProvider = provider19;
        this.statusBarServiceProvider = provider20;
        this.notificationShadeWindowControllerProvider = provider21;
        this.iWindowManagerProvider = provider22;
        this.backgroundExecutorProvider = provider23;
        this.uiEventLoggerProvider = provider24;
        this.ringerModeTrackerProvider = provider25;
        this.sysUiStateProvider = provider26;
        this.handlerProvider = provider27;
        this.packageManagerProvider = provider28;
        this.statusBarOptionalProvider = provider29;
        this.keyguardUpdateMonitorProvider = provider30;
        this.dialogLaunchAnimatorProvider = provider31;
        this.dialogManagerProvider = provider32;
    }

    public static GlobalActionsDialogLite_Factory create(Provider provider, DelegateFactory delegateFactory, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, Provider provider11, Provider provider12, Provider provider13, Provider provider14, Provider provider15, Provider provider16, Provider provider17, Provider provider18, Provider provider19, Provider provider20, Provider provider21, Provider provider22, Provider provider23, Provider provider24, Provider provider25, Provider provider26, Provider provider27, Provider provider28, Provider provider29, Provider provider30, Provider provider31, Provider provider32) {
        return new GlobalActionsDialogLite_Factory(provider, delegateFactory, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new GlobalActionsDialogLite(this.contextProvider.mo144get(), this.windowManagerFuncsProvider.mo144get(), this.audioManagerProvider.mo144get(), this.iDreamManagerProvider.mo144get(), this.devicePolicyManagerProvider.mo144get(), this.lockPatternUtilsProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.telephonyListenerManagerProvider.mo144get(), this.globalSettingsProvider.mo144get(), this.secureSettingsProvider.mo144get(), this.vibratorProvider.mo144get(), this.resourcesProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.userManagerProvider.mo144get(), this.trustManagerProvider.mo144get(), this.iActivityManagerProvider.mo144get(), this.telecomManagerProvider.mo144get(), this.metricsLoggerProvider.mo144get(), this.colorExtractorProvider.mo144get(), this.statusBarServiceProvider.mo144get(), this.notificationShadeWindowControllerProvider.mo144get(), this.iWindowManagerProvider.mo144get(), this.backgroundExecutorProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.ringerModeTrackerProvider.mo144get(), this.sysUiStateProvider.mo144get(), this.handlerProvider.mo144get(), this.packageManagerProvider.mo144get(), this.statusBarOptionalProvider.mo144get(), this.keyguardUpdateMonitorProvider.mo144get(), this.dialogLaunchAnimatorProvider.mo144get(), this.dialogManagerProvider.mo144get());
    }
}
