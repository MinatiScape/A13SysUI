package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ShortcutBarController_Factory implements Factory<ShortcutBarController> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<FpsController> fpsControllerProvider;
    public final Provider<ScreenRecordController> screenRecordControllerProvider;
    public final Provider<Optional<TaskSurfaceHelper>> screenshotControllerProvider;
    public final Provider<Handler> screenshotHandlerProvider;
    public final Provider<ToastController> toastProvider;
    public final Provider<GameDashboardUiEventLogger> uiEventLoggerProvider;
    public final Provider<WindowManager> windowManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ShortcutBarController(this.contextProvider.mo144get(), this.windowManagerProvider.mo144get(), this.fpsControllerProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.screenshotHandlerProvider.mo144get(), this.screenRecordControllerProvider.mo144get(), this.screenshotControllerProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.toastProvider.mo144get());
    }

    public ShortcutBarController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, GameDashboardUiEventLogger_Factory gameDashboardUiEventLogger_Factory, Provider provider8) {
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
        this.fpsControllerProvider = provider3;
        this.configurationControllerProvider = provider4;
        this.screenshotHandlerProvider = provider5;
        this.screenRecordControllerProvider = provider6;
        this.screenshotControllerProvider = provider7;
        this.uiEventLoggerProvider = gameDashboardUiEventLogger_Factory;
        this.toastProvider = provider8;
    }
}
