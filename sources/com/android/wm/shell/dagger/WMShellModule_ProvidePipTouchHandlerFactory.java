package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipMotionHelper;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellModule_ProvidePipTouchHandlerFactory implements Factory<PipTouchHandler> {
    public final Provider<Context> contextProvider;
    public final Provider<FloatingContentCoordinator> floatingContentCoordinatorProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<PhonePipMenuController> menuPhoneControllerProvider;
    public final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    public final Provider<PipBoundsState> pipBoundsStateProvider;
    public final Provider<PipMotionHelper> pipMotionHelperProvider;
    public final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;
    public final Provider<PipUiEventLogger> pipUiEventLoggerProvider;

    public static WMShellModule_ProvidePipTouchHandlerFactory create(Provider<Context> provider, Provider<PhonePipMenuController> provider2, Provider<PipBoundsAlgorithm> provider3, Provider<PipBoundsState> provider4, Provider<PipTaskOrganizer> provider5, Provider<PipMotionHelper> provider6, Provider<FloatingContentCoordinator> provider7, Provider<PipUiEventLogger> provider8, Provider<ShellExecutor> provider9) {
        return new WMShellModule_ProvidePipTouchHandlerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PipTouchHandler(this.contextProvider.mo144get(), this.menuPhoneControllerProvider.mo144get(), this.pipBoundsAlgorithmProvider.mo144get(), this.pipBoundsStateProvider.mo144get(), this.pipTaskOrganizerProvider.mo144get(), this.pipMotionHelperProvider.mo144get(), this.floatingContentCoordinatorProvider.mo144get(), this.pipUiEventLoggerProvider.mo144get(), this.mainExecutorProvider.mo144get());
    }

    public WMShellModule_ProvidePipTouchHandlerFactory(Provider<Context> provider, Provider<PhonePipMenuController> provider2, Provider<PipBoundsAlgorithm> provider3, Provider<PipBoundsState> provider4, Provider<PipTaskOrganizer> provider5, Provider<PipMotionHelper> provider6, Provider<FloatingContentCoordinator> provider7, Provider<PipUiEventLogger> provider8, Provider<ShellExecutor> provider9) {
        this.contextProvider = provider;
        this.menuPhoneControllerProvider = provider2;
        this.pipBoundsAlgorithmProvider = provider3;
        this.pipBoundsStateProvider = provider4;
        this.pipTaskOrganizerProvider = provider5;
        this.pipMotionHelperProvider = provider6;
        this.floatingContentCoordinatorProvider = provider7;
        this.pipUiEventLoggerProvider = provider8;
        this.mainExecutorProvider = provider9;
    }
}
