package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellModule_ProvidePipTaskOrganizerFactory implements Factory<PipTaskOrganizer> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<PhonePipMenuController> menuPhoneControllerProvider;
    public final Provider<Optional<SplitScreenController>> newSplitScreenOptionalProvider;
    public final Provider<PipAnimationController> pipAnimationControllerProvider;
    public final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    public final Provider<PipBoundsState> pipBoundsStateProvider;
    public final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;
    public final Provider<PipTransitionController> pipTransitionControllerProvider;
    public final Provider<PipTransitionState> pipTransitionStateProvider;
    public final Provider<PipUiEventLogger> pipUiEventLoggerProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<Optional<LegacySplitScreenController>> splitScreenOptionalProvider;
    public final Provider<SyncTransactionQueue> syncTransactionQueueProvider;

    public static WMShellModule_ProvidePipTaskOrganizerFactory create(Provider<Context> provider, Provider<SyncTransactionQueue> provider2, Provider<PipTransitionState> provider3, Provider<PipBoundsState> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PhonePipMenuController> provider6, Provider<PipAnimationController> provider7, Provider<PipSurfaceTransactionHelper> provider8, Provider<PipTransitionController> provider9, Provider<Optional<LegacySplitScreenController>> provider10, Provider<Optional<SplitScreenController>> provider11, Provider<DisplayController> provider12, Provider<PipUiEventLogger> provider13, Provider<ShellTaskOrganizer> provider14, Provider<ShellExecutor> provider15) {
        return new WMShellModule_ProvidePipTaskOrganizerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PipTaskOrganizer(this.contextProvider.mo144get(), this.syncTransactionQueueProvider.mo144get(), this.pipTransitionStateProvider.mo144get(), this.pipBoundsStateProvider.mo144get(), this.pipBoundsAlgorithmProvider.mo144get(), this.menuPhoneControllerProvider.mo144get(), this.pipAnimationControllerProvider.mo144get(), this.pipSurfaceTransactionHelperProvider.mo144get(), this.pipTransitionControllerProvider.mo144get(), this.splitScreenOptionalProvider.mo144get(), this.newSplitScreenOptionalProvider.mo144get(), this.displayControllerProvider.mo144get(), this.pipUiEventLoggerProvider.mo144get(), this.shellTaskOrganizerProvider.mo144get(), this.mainExecutorProvider.mo144get());
    }

    public WMShellModule_ProvidePipTaskOrganizerFactory(Provider<Context> provider, Provider<SyncTransactionQueue> provider2, Provider<PipTransitionState> provider3, Provider<PipBoundsState> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PhonePipMenuController> provider6, Provider<PipAnimationController> provider7, Provider<PipSurfaceTransactionHelper> provider8, Provider<PipTransitionController> provider9, Provider<Optional<LegacySplitScreenController>> provider10, Provider<Optional<SplitScreenController>> provider11, Provider<DisplayController> provider12, Provider<PipUiEventLogger> provider13, Provider<ShellTaskOrganizer> provider14, Provider<ShellExecutor> provider15) {
        this.contextProvider = provider;
        this.syncTransactionQueueProvider = provider2;
        this.pipTransitionStateProvider = provider3;
        this.pipBoundsStateProvider = provider4;
        this.pipBoundsAlgorithmProvider = provider5;
        this.menuPhoneControllerProvider = provider6;
        this.pipAnimationControllerProvider = provider7;
        this.pipSurfaceTransactionHelperProvider = provider8;
        this.pipTransitionControllerProvider = provider9;
        this.splitScreenOptionalProvider = provider10;
        this.newSplitScreenOptionalProvider = provider11;
        this.displayControllerProvider = provider12;
        this.pipUiEventLoggerProvider = provider13;
        this.shellTaskOrganizerProvider = provider14;
        this.mainExecutorProvider = provider15;
    }
}
