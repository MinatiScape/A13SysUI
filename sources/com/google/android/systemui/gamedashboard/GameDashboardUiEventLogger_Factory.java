package com.google.android.systemui.gamedashboard;

import android.animation.AnimationHandler;
import com.android.internal.logging.UiEventLogger;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import com.android.wm.shell.common.ShellExecutor;
import com.google.android.systemui.assist.uihints.OverlappedElementController;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GameDashboardUiEventLogger_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider uiEventLoggerProvider;

    public /* synthetic */ GameDashboardUiEventLogger_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.uiEventLoggerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new GameDashboardUiEventLogger((UiEventLogger) this.uiEventLoggerProvider.mo144get());
            case 1:
                ShellExecutor shellExecutor = (ShellExecutor) this.uiEventLoggerProvider.mo144get();
                try {
                    AnimationHandler animationHandler = new AnimationHandler();
                    shellExecutor.executeBlocking$1(new WifiEntry$$ExternalSyntheticLambda2(animationHandler, 11));
                    return animationHandler;
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed to initialize SfVsync animation handler in 1s", e);
                }
            default:
                return new OverlappedElementController(DoubleCheck.lazy(this.uiEventLoggerProvider));
        }
    }
}
