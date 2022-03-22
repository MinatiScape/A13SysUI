package com.android.wm.shell.dagger;

import com.android.wm.shell.ShellCommandHandlerImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideShellCommandHandlerImplFactory implements Factory<ShellCommandHandlerImpl> {
    public final Provider<Optional<AppPairsController>> appPairsOptionalProvider;
    public final Provider<Optional<HideDisplayCutoutController>> hideDisplayCutoutProvider;
    public final Provider<Optional<LegacySplitScreenController>> legacySplitScreenOptionalProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<OneHandedController>> oneHandedOptionalProvider;
    public final Provider<Optional<Pip>> pipOptionalProvider;
    public final Provider<Optional<RecentTasksController>> recentTasksOptionalProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<Optional<SplitScreenController>> splitScreenOptionalProvider;

    public static WMShellBaseModule_ProvideShellCommandHandlerImplFactory create(Provider<ShellTaskOrganizer> provider, Provider<Optional<LegacySplitScreenController>> provider2, Provider<Optional<SplitScreenController>> provider3, Provider<Optional<Pip>> provider4, Provider<Optional<OneHandedController>> provider5, Provider<Optional<HideDisplayCutoutController>> provider6, Provider<Optional<AppPairsController>> provider7, Provider<Optional<RecentTasksController>> provider8, Provider<ShellExecutor> provider9) {
        return new WMShellBaseModule_ProvideShellCommandHandlerImplFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ShellCommandHandlerImpl(this.shellTaskOrganizerProvider.mo144get(), this.legacySplitScreenOptionalProvider.mo144get(), this.splitScreenOptionalProvider.mo144get(), this.pipOptionalProvider.mo144get(), this.oneHandedOptionalProvider.mo144get(), this.hideDisplayCutoutProvider.mo144get(), this.appPairsOptionalProvider.mo144get(), this.recentTasksOptionalProvider.mo144get(), this.mainExecutorProvider.mo144get());
    }

    public WMShellBaseModule_ProvideShellCommandHandlerImplFactory(Provider<ShellTaskOrganizer> provider, Provider<Optional<LegacySplitScreenController>> provider2, Provider<Optional<SplitScreenController>> provider3, Provider<Optional<Pip>> provider4, Provider<Optional<OneHandedController>> provider5, Provider<Optional<HideDisplayCutoutController>> provider6, Provider<Optional<AppPairsController>> provider7, Provider<Optional<RecentTasksController>> provider8, Provider<ShellExecutor> provider9) {
        this.shellTaskOrganizerProvider = provider;
        this.legacySplitScreenOptionalProvider = provider2;
        this.splitScreenOptionalProvider = provider3;
        this.pipOptionalProvider = provider4;
        this.oneHandedOptionalProvider = provider5;
        this.hideDisplayCutoutProvider = provider6;
        this.appPairsOptionalProvider = provider7;
        this.recentTasksOptionalProvider = provider8;
        this.mainExecutorProvider = provider9;
    }
}
