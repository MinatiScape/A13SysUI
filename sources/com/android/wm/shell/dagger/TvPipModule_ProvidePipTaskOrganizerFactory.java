package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvPipModule_ProvidePipTaskOrganizerFactory implements Factory<PipTaskOrganizer> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<SplitScreenController>> newSplitScreenOptionalProvider;
    public final Provider<PipAnimationController> pipAnimationControllerProvider;
    public final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;
    public final Provider<PipTransitionController> pipTransitionControllerProvider;
    public final Provider<PipTransitionState> pipTransitionStateProvider;
    public final Provider<PipUiEventLogger> pipUiEventLoggerProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<Optional<LegacySplitScreenController>> splitScreenOptionalProvider;
    public final Provider<SyncTransactionQueue> syncTransactionQueueProvider;
    public final Provider<TvPipBoundsAlgorithm> tvPipBoundsAlgorithmProvider;
    public final Provider<TvPipBoundsState> tvPipBoundsStateProvider;
    public final Provider<TvPipMenuController> tvPipMenuControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Context context = this.contextProvider.mo144get();
        TvPipMenuController tvPipMenuController = this.tvPipMenuControllerProvider.mo144get();
        SyncTransactionQueue syncTransactionQueue = this.syncTransactionQueueProvider.mo144get();
        TvPipBoundsState tvPipBoundsState = this.tvPipBoundsStateProvider.mo144get();
        return new PipTaskOrganizer(context, syncTransactionQueue, this.pipTransitionStateProvider.mo144get(), tvPipBoundsState, this.tvPipBoundsAlgorithmProvider.mo144get(), tvPipMenuController, this.pipAnimationControllerProvider.mo144get(), this.pipSurfaceTransactionHelperProvider.mo144get(), this.pipTransitionControllerProvider.mo144get(), this.splitScreenOptionalProvider.mo144get(), this.newSplitScreenOptionalProvider.mo144get(), this.displayControllerProvider.mo144get(), this.pipUiEventLoggerProvider.mo144get(), this.shellTaskOrganizerProvider.mo144get(), this.mainExecutorProvider.mo144get());
    }

    public TvPipModule_ProvidePipTaskOrganizerFactory(Provider<Context> provider, Provider<TvPipMenuController> provider2, Provider<SyncTransactionQueue> provider3, Provider<TvPipBoundsState> provider4, Provider<PipTransitionState> provider5, Provider<TvPipBoundsAlgorithm> provider6, Provider<PipAnimationController> provider7, Provider<PipTransitionController> provider8, Provider<PipSurfaceTransactionHelper> provider9, Provider<Optional<LegacySplitScreenController>> provider10, Provider<Optional<SplitScreenController>> provider11, Provider<DisplayController> provider12, Provider<PipUiEventLogger> provider13, Provider<ShellTaskOrganizer> provider14, Provider<ShellExecutor> provider15) {
        this.contextProvider = provider;
        this.tvPipMenuControllerProvider = provider2;
        this.syncTransactionQueueProvider = provider3;
        this.tvPipBoundsStateProvider = provider4;
        this.pipTransitionStateProvider = provider5;
        this.tvPipBoundsAlgorithmProvider = provider6;
        this.pipAnimationControllerProvider = provider7;
        this.pipTransitionControllerProvider = provider8;
        this.pipSurfaceTransactionHelperProvider = provider9;
        this.splitScreenOptionalProvider = provider10;
        this.newSplitScreenOptionalProvider = provider11;
        this.displayControllerProvider = provider12;
        this.pipUiEventLoggerProvider = provider13;
        this.shellTaskOrganizerProvider = provider14;
        this.mainExecutorProvider = provider15;
    }
}
