package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.content.om.OverlayManager;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.theme.ThemeOverlayApplier;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ToastController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider configurationControllerProvider;
    public final Provider contextProvider;
    public final Provider navigationModeControllerProvider;
    public final Provider uiEventLoggerProvider;
    public final Provider windowManagerProvider;

    public /* synthetic */ ToastController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.configurationControllerProvider = provider2;
        this.windowManagerProvider = provider3;
        this.uiEventLoggerProvider = provider4;
        this.navigationModeControllerProvider = provider5;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ToastController((Context) this.contextProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get(), (WindowManager) this.windowManagerProvider.mo144get(), (UiEventLogger) this.uiEventLoggerProvider.mo144get(), (NavigationModeController) this.navigationModeControllerProvider.mo144get());
            default:
                Context context = (Context) this.contextProvider.mo144get();
                return new ThemeOverlayApplier((OverlayManager) this.uiEventLoggerProvider.mo144get(), (Executor) this.configurationControllerProvider.mo144get(), (Executor) this.windowManagerProvider.mo144get(), context.getString(2131952611), context.getString(2131953361), (DumpManager) this.navigationModeControllerProvider.mo144get());
        }
    }
}
