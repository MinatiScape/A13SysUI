package com.google.android.systemui.columbus.sensors;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.feedback.SquishyNavigationButtons;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GestureController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider commandRegistryProvider;
    public final Provider gestureSensorProvider;
    public final Provider softGatesProvider;
    public final Provider uiEventLoggerProvider;

    public /* synthetic */ GestureController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, int i) {
        this.$r8$classId = i;
        this.gestureSensorProvider = provider;
        this.softGatesProvider = provider2;
        this.commandRegistryProvider = provider3;
        this.uiEventLoggerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new GestureController((GestureSensor) this.gestureSensorProvider.mo144get(), (Set) this.softGatesProvider.mo144get(), (CommandRegistry) this.commandRegistryProvider.mo144get(), (UiEventLogger) this.uiEventLoggerProvider.mo144get());
            default:
                return new SquishyNavigationButtons((Context) this.gestureSensorProvider.mo144get(), (KeyguardViewMediator) this.softGatesProvider.mo144get(), (StatusBar) this.commandRegistryProvider.mo144get(), (NavigationModeController) this.uiEventLoggerProvider.mo144get());
        }
    }
}
