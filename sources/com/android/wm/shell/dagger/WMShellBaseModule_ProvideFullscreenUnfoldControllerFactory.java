package com.android.wm.shell.dagger;

import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory implements Factory<Optional<FullscreenUnfoldController>> {
    public final Provider<Optional<FullscreenUnfoldController>> fullscreenUnfoldControllerProvider;
    public final Provider<Optional<ShellUnfoldProgressProvider>> progressProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Optional<FullscreenUnfoldController> optional = this.fullscreenUnfoldControllerProvider.mo144get();
        Optional<ShellUnfoldProgressProvider> optional2 = this.progressProvider.mo144get();
        if (!optional2.isPresent() || optional2.get() == ShellUnfoldProgressProvider.NO_PROVIDER) {
            optional = Optional.empty();
        }
        Objects.requireNonNull(optional, "Cannot return null from a non-@Nullable @Provides method");
        return optional;
    }

    public WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory(Provider<Optional<FullscreenUnfoldController>> provider, Provider<Optional<ShellUnfoldProgressProvider>> provider2) {
        this.fullscreenUnfoldControllerProvider = provider;
        this.progressProvider = provider2;
    }
}
