package com.android.wm.shell.dagger;

import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.pip.tv.TvPipTransition;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvPipModule_ProvideTvPipTransitionFactory implements Factory<PipTransitionController> {
    public final Provider<PipAnimationController> pipAnimationControllerProvider;
    public final Provider<TvPipMenuController> pipMenuControllerProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<Transitions> transitionsProvider;
    public final Provider<TvPipBoundsAlgorithm> tvPipBoundsAlgorithmProvider;
    public final Provider<TvPipBoundsState> tvPipBoundsStateProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Transitions transitions = this.transitionsProvider.mo144get();
        this.shellTaskOrganizerProvider.mo144get();
        PipAnimationController pipAnimationController = this.pipAnimationControllerProvider.mo144get();
        return new TvPipTransition(this.tvPipBoundsStateProvider.mo144get(), this.pipMenuControllerProvider.mo144get(), this.tvPipBoundsAlgorithmProvider.mo144get(), pipAnimationController, transitions);
    }

    public TvPipModule_ProvideTvPipTransitionFactory(Provider<Transitions> provider, Provider<ShellTaskOrganizer> provider2, Provider<PipAnimationController> provider3, Provider<TvPipBoundsAlgorithm> provider4, Provider<TvPipBoundsState> provider5, Provider<TvPipMenuController> provider6) {
        this.transitionsProvider = provider;
        this.shellTaskOrganizerProvider = provider2;
        this.pipAnimationControllerProvider = provider3;
        this.tvPipBoundsAlgorithmProvider = provider4;
        this.tvPipBoundsStateProvider = provider5;
        this.pipMenuControllerProvider = provider6;
    }
}
