package com.android.systemui.controls.management;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.splitscreen.StageTaskUnfoldController;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldBackgroundController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Function;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsProviderSelectorActivity_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider backExecutorProvider;
    public final Provider broadcastDispatcherProvider;
    public final Provider controlsControllerProvider;
    public final Provider executorProvider;
    public final Provider listingControllerProvider;
    public final Provider uiControllerProvider;

    public /* synthetic */ ControlsProviderSelectorActivity_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, int i) {
        this.$r8$classId = i;
        this.executorProvider = provider;
        this.backExecutorProvider = provider2;
        this.listingControllerProvider = provider3;
        this.controlsControllerProvider = provider4;
        this.broadcastDispatcherProvider = provider5;
        this.uiControllerProvider = provider6;
    }

    public static ControlsProviderSelectorActivity_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6) {
        return new ControlsProviderSelectorActivity_Factory(provider, provider2, provider3, provider4, provider5, provider6, 0);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ControlsProviderSelectorActivity((Executor) this.executorProvider.mo144get(), (Executor) this.backExecutorProvider.mo144get(), (ControlsListingController) this.listingControllerProvider.mo144get(), (ControlsController) this.controlsControllerProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get(), (ControlsUiController) this.uiControllerProvider.mo144get());
            default:
                final Context context = (Context) this.backExecutorProvider.mo144get();
                final TransactionPool transactionPool = (TransactionPool) this.listingControllerProvider.mo144get();
                final Lazy lazy = DoubleCheck.lazy(this.controlsControllerProvider);
                final DisplayInsetsController displayInsetsController = (DisplayInsetsController) this.broadcastDispatcherProvider.mo144get();
                final ShellExecutor shellExecutor = (ShellExecutor) this.uiControllerProvider.mo144get();
                Optional map = ((Optional) this.executorProvider.mo144get()).map(new Function() { // from class: com.android.wm.shell.dagger.WMShellModule$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        Context context2 = context;
                        TransactionPool transactionPool2 = transactionPool;
                        DisplayInsetsController displayInsetsController2 = displayInsetsController;
                        Lazy lazy2 = lazy;
                        return new StageTaskUnfoldController(context2, transactionPool2, (ShellUnfoldProgressProvider) obj, displayInsetsController2, (UnfoldBackgroundController) lazy2.get(), shellExecutor);
                    }
                });
                Objects.requireNonNull(map, "Cannot return null from a non-@Nullable @Provides method");
                return map;
        }
    }
}
