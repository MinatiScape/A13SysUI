package com.android.systemui.screenshot;

import android.app.ActivityManager;
import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScreenshotController_Factory implements Factory<ScreenshotController> {
    public final Provider<ActivityManager> activityManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ImageExporter> imageExporterProvider;
    public final Provider<LongScreenshotData> longScreenshotHolderProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<ScreenshotNotificationsController> screenshotNotificationsControllerProvider;
    public final Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;
    public final Provider<ScrollCaptureClient> scrollCaptureClientProvider;
    public final Provider<ScrollCaptureController> scrollCaptureControllerProvider;
    public final Provider<TimeoutHandler> timeoutHandlerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public static ScreenshotController_Factory create(Provider<Context> provider, Provider<ScreenshotSmartActions> provider2, Provider<ScreenshotNotificationsController> provider3, Provider<ScrollCaptureClient> provider4, Provider<UiEventLogger> provider5, Provider<ImageExporter> provider6, Provider<Executor> provider7, Provider<ScrollCaptureController> provider8, Provider<LongScreenshotData> provider9, Provider<ActivityManager> provider10, Provider<TimeoutHandler> provider11) {
        return new ScreenshotController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ScreenshotController(this.contextProvider.mo144get(), this.screenshotSmartActionsProvider.mo144get(), this.screenshotNotificationsControllerProvider.mo144get(), this.scrollCaptureClientProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.imageExporterProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.scrollCaptureControllerProvider.mo144get(), this.longScreenshotHolderProvider.mo144get(), this.activityManagerProvider.mo144get(), this.timeoutHandlerProvider.mo144get());
    }

    public ScreenshotController_Factory(Provider<Context> provider, Provider<ScreenshotSmartActions> provider2, Provider<ScreenshotNotificationsController> provider3, Provider<ScrollCaptureClient> provider4, Provider<UiEventLogger> provider5, Provider<ImageExporter> provider6, Provider<Executor> provider7, Provider<ScrollCaptureController> provider8, Provider<LongScreenshotData> provider9, Provider<ActivityManager> provider10, Provider<TimeoutHandler> provider11) {
        this.contextProvider = provider;
        this.screenshotSmartActionsProvider = provider2;
        this.screenshotNotificationsControllerProvider = provider3;
        this.scrollCaptureClientProvider = provider4;
        this.uiEventLoggerProvider = provider5;
        this.imageExporterProvider = provider6;
        this.mainExecutorProvider = provider7;
        this.scrollCaptureControllerProvider = provider8;
        this.longScreenshotHolderProvider = provider9;
        this.activityManagerProvider = provider10;
        this.timeoutHandlerProvider = provider11;
    }
}
