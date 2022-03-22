package com.android.wm.shell.dagger;

import android.content.Context;
import android.hardware.display.ColorDisplayManager;
import android.os.Handler;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvPipModule_ProvidesTvPipMenuControllerFactory implements Factory {
    public final /* synthetic */ int $r8$classId = 1;
    public final Provider contextProvider;
    public final Object mainHandlerProvider;
    public final Provider pipMediaControllerProvider;
    public final Provider systemWindowsProvider;
    public final Provider tvPipBoundsStateProvider;

    public TvPipModule_ProvidesTvPipMenuControllerFactory(DependencyProvider dependencyProvider, Provider provider, Provider provider2, Provider provider3, Provider provider4) {
        this.mainHandlerProvider = dependencyProvider;
        this.contextProvider = provider;
        this.tvPipBoundsStateProvider = provider2;
        this.systemWindowsProvider = provider3;
        this.pipMediaControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new TvPipMenuController((Context) this.contextProvider.mo144get(), (TvPipBoundsState) this.tvPipBoundsStateProvider.mo144get(), (SystemWindows) this.systemWindowsProvider.mo144get(), (PipMediaController) this.pipMediaControllerProvider.mo144get(), (Handler) ((Provider) this.mainHandlerProvider).mo144get());
            default:
                UserTracker userTracker = (UserTracker) this.tvPipBoundsStateProvider.mo144get();
                Objects.requireNonNull((DependencyProvider) this.mainHandlerProvider);
                return new ReduceBrightColorsController(userTracker, (Handler) this.contextProvider.mo144get(), (ColorDisplayManager) this.systemWindowsProvider.mo144get(), (SecureSettings) this.pipMediaControllerProvider.mo144get());
        }
    }

    public TvPipModule_ProvidesTvPipMenuControllerFactory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        this.contextProvider = provider;
        this.tvPipBoundsStateProvider = provider2;
        this.systemWindowsProvider = provider3;
        this.pipMediaControllerProvider = provider4;
        this.mainHandlerProvider = provider5;
    }
}
