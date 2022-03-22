package com.android.systemui.screenshot;

import android.content.Context;
import android.view.WindowManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScreenshotNotificationsController_Factory implements Factory<ScreenshotNotificationsController> {
    public final Provider<Context> contextProvider;
    public final Provider<WindowManager> windowManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ScreenshotNotificationsController(this.contextProvider.mo144get(), this.windowManagerProvider.mo144get());
    }

    public ScreenshotNotificationsController_Factory(Provider<Context> provider, Provider<WindowManager> provider2) {
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
    }
}
