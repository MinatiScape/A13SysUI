package com.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CameraToggleTile_Factory implements Factory<CameraToggleTile> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> backgroundLooperProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<QSHost> hostProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<IndividualSensorPrivacyController> sensorPrivacyControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public static CameraToggleTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<MetricsLogger> provider4, Provider<FalsingManager> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<IndividualSensorPrivacyController> provider9, Provider<KeyguardStateController> provider10) {
        return new CameraToggleTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new CameraToggleTile(this.hostProvider.mo144get(), this.backgroundLooperProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.metricsLoggerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.qsLoggerProvider.mo144get(), this.sensorPrivacyControllerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get());
    }

    public CameraToggleTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<MetricsLogger> provider4, Provider<FalsingManager> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<IndividualSensorPrivacyController> provider9, Provider<KeyguardStateController> provider10) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.metricsLoggerProvider = provider4;
        this.falsingManagerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.sensorPrivacyControllerProvider = provider9;
        this.keyguardStateControllerProvider = provider10;
    }
}
