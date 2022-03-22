package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTransition;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipTransitionState;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellModule_ProvidePipTransitionControllerFactory implements Factory<PipTransitionController> {
    public final Provider<Context> contextProvider;
    public final Provider<PipAnimationController> pipAnimationControllerProvider;
    public final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    public final Provider<PipBoundsState> pipBoundsStateProvider;
    public final Provider<PhonePipMenuController> pipMenuControllerProvider;
    public final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;
    public final Provider<PipTransitionState> pipTransitionStateProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<Optional<SplitScreenController>> splitScreenOptionalProvider;
    public final Provider<Transitions> transitionsProvider;

    public static WMShellModule_ProvidePipTransitionControllerFactory create(Provider<Context> provider, Provider<Transitions> provider2, Provider<ShellTaskOrganizer> provider3, Provider<PipAnimationController> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PipBoundsState> provider6, Provider<PipTransitionState> provider7, Provider<PhonePipMenuController> provider8, Provider<PipSurfaceTransactionHelper> provider9, Provider<Optional<SplitScreenController>> provider10) {
        return new WMShellModule_ProvidePipTransitionControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Context context = this.contextProvider.mo144get();
        Transitions transitions = this.transitionsProvider.mo144get();
        this.shellTaskOrganizerProvider.mo144get();
        PipAnimationController pipAnimationController = this.pipAnimationControllerProvider.mo144get();
        return new PipTransition(context, this.pipBoundsStateProvider.mo144get(), this.pipTransitionStateProvider.mo144get(), this.pipMenuControllerProvider.mo144get(), this.pipBoundsAlgorithmProvider.mo144get(), pipAnimationController, transitions, this.pipSurfaceTransactionHelperProvider.mo144get(), this.splitScreenOptionalProvider.mo144get());
    }

    public WMShellModule_ProvidePipTransitionControllerFactory(Provider<Context> provider, Provider<Transitions> provider2, Provider<ShellTaskOrganizer> provider3, Provider<PipAnimationController> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PipBoundsState> provider6, Provider<PipTransitionState> provider7, Provider<PhonePipMenuController> provider8, Provider<PipSurfaceTransactionHelper> provider9, Provider<Optional<SplitScreenController>> provider10) {
        this.contextProvider = provider;
        this.transitionsProvider = provider2;
        this.shellTaskOrganizerProvider = provider3;
        this.pipAnimationControllerProvider = provider4;
        this.pipBoundsAlgorithmProvider = provider5;
        this.pipBoundsStateProvider = provider6;
        this.pipTransitionStateProvider = provider7;
        this.pipMenuControllerProvider = provider8;
        this.pipSurfaceTransactionHelperProvider = provider9;
        this.splitScreenOptionalProvider = provider10;
    }
}
