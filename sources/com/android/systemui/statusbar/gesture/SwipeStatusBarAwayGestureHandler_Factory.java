package com.android.systemui.statusbar.gesture;

import android.content.Context;
import com.android.systemui.screenshot.SmartActionsReceiver_Factory;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SwipeStatusBarAwayGestureHandler_Factory implements Factory<SwipeStatusBarAwayGestureHandler> {
    public final Provider<Context> contextProvider;
    public final Provider<SwipeStatusBarAwayGestureLogger> loggerProvider;
    public final Provider<StatusBarWindowController> statusBarWindowControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SwipeStatusBarAwayGestureHandler(this.contextProvider.mo144get(), this.statusBarWindowControllerProvider.mo144get(), this.loggerProvider.mo144get());
    }

    public SwipeStatusBarAwayGestureHandler_Factory(Provider provider, Provider provider2, SmartActionsReceiver_Factory smartActionsReceiver_Factory) {
        this.contextProvider = provider;
        this.statusBarWindowControllerProvider = provider2;
        this.loggerProvider = smartActionsReceiver_Factory;
    }
}
