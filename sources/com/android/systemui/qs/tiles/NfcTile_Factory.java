package com.android.systemui.qs.tiles;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.WindowManager;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.biometrics.SidefpsController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NfcTile_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider activityStarterProvider;
    public final Provider backgroundLooperProvider;
    public final Provider broadcastDispatcherProvider;
    public final Provider falsingManagerProvider;
    public final Provider hostProvider;
    public final Provider mainHandlerProvider;
    public final Provider metricsLoggerProvider;
    public final Provider qsLoggerProvider;
    public final Provider statusBarStateControllerProvider;

    public /* synthetic */ NfcTile_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, int i) {
        this.$r8$classId = i;
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.broadcastDispatcherProvider = provider9;
    }

    public static NfcTile_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9) {
        return new NfcTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, 1);
    }

    public static NfcTile_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9) {
        return new NfcTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, 0);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new NfcTile((QSHost) this.hostProvider.mo144get(), (Looper) this.backgroundLooperProvider.mo144get(), (Handler) this.mainHandlerProvider.mo144get(), (FalsingManager) this.falsingManagerProvider.mo144get(), (MetricsLogger) this.metricsLoggerProvider.mo144get(), (StatusBarStateController) this.statusBarStateControllerProvider.mo144get(), (ActivityStarter) this.activityStarterProvider.mo144get(), (QSLogger) this.qsLoggerProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get());
            default:
                return new SidefpsController((Context) this.hostProvider.mo144get(), (LayoutInflater) this.backgroundLooperProvider.mo144get(), (FingerprintManager) this.mainHandlerProvider.mo144get(), (WindowManager) this.falsingManagerProvider.mo144get(), (ActivityTaskManager) this.metricsLoggerProvider.mo144get(), (OverviewProxyService) this.statusBarStateControllerProvider.mo144get(), (DisplayManager) this.activityStarterProvider.mo144get(), (DelayableExecutor) this.qsLoggerProvider.mo144get(), (Handler) this.broadcastDispatcherProvider.mo144get());
        }
    }
}
