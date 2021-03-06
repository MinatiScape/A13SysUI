package com.android.systemui.privacy;

import android.content.pm.PackageManager;
import android.permission.PermissionManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PrivacyDialogController_Factory implements Factory<PrivacyDialogController> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<AppOpsController> appOpsControllerProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<PermissionManager> permissionManagerProvider;
    public final Provider<PrivacyItemController> privacyItemControllerProvider;
    public final Provider<PrivacyLogger> privacyLoggerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<Executor> uiExecutorProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public static PrivacyDialogController_Factory create(Provider<PermissionManager> provider, Provider<PackageManager> provider2, Provider<PrivacyItemController> provider3, Provider<UserTracker> provider4, Provider<ActivityStarter> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<PrivacyLogger> provider8, Provider<KeyguardStateController> provider9, Provider<AppOpsController> provider10, Provider<UiEventLogger> provider11) {
        return new PrivacyDialogController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PrivacyDialogController(this.permissionManagerProvider.mo144get(), this.packageManagerProvider.mo144get(), this.privacyItemControllerProvider.mo144get(), this.userTrackerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.backgroundExecutorProvider.mo144get(), this.uiExecutorProvider.mo144get(), this.privacyLoggerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.appOpsControllerProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), PrivacyDialogControllerKt.defaultDialogProvider);
    }

    public PrivacyDialogController_Factory(Provider<PermissionManager> provider, Provider<PackageManager> provider2, Provider<PrivacyItemController> provider3, Provider<UserTracker> provider4, Provider<ActivityStarter> provider5, Provider<Executor> provider6, Provider<Executor> provider7, Provider<PrivacyLogger> provider8, Provider<KeyguardStateController> provider9, Provider<AppOpsController> provider10, Provider<UiEventLogger> provider11) {
        this.permissionManagerProvider = provider;
        this.packageManagerProvider = provider2;
        this.privacyItemControllerProvider = provider3;
        this.userTrackerProvider = provider4;
        this.activityStarterProvider = provider5;
        this.backgroundExecutorProvider = provider6;
        this.uiExecutorProvider = provider7;
        this.privacyLoggerProvider = provider8;
        this.keyguardStateControllerProvider = provider9;
        this.appOpsControllerProvider = provider10;
        this.uiEventLoggerProvider = provider11;
    }
}
