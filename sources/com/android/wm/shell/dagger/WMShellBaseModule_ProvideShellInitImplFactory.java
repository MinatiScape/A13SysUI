package com.android.wm.shell.dagger;

import com.android.wm.shell.ShellInitImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.freeform.FreeformTaskListener;
import com.android.wm.shell.fullscreen.FullscreenTaskListener;
import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideShellInitImplFactory implements Factory<ShellInitImpl> {
    public final Provider<Optional<AppPairsController>> appPairsOptionalProvider;
    public final Provider<Optional<FullscreenUnfoldController>> appUnfoldTransitionControllerProvider;
    public final Provider<Optional<BubbleController>> bubblesOptionalProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<DisplayImeController> displayImeControllerProvider;
    public final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    public final Provider<DragAndDropController> dragAndDropControllerProvider;
    public final Provider<Optional<FreeformTaskListener>> freeformTaskListenerProvider;
    public final Provider<FullscreenTaskListener> fullscreenTaskListenerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<PipTouchHandler>> pipTouchHandlerOptionalProvider;
    public final Provider<Optional<RecentTasksController>> recentTasksOptionalProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<Optional<SplitScreenController>> splitScreenOptionalProvider;
    public final Provider<StartingWindowController> startingWindowProvider;
    public final Provider<Transitions> transitionsProvider;

    public WMShellBaseModule_ProvideShellInitImplFactory(Provider<DisplayController> provider, Provider<DisplayImeController> provider2, Provider<DisplayInsetsController> provider3, Provider<DragAndDropController> provider4, Provider<ShellTaskOrganizer> provider5, Provider<Optional<BubbleController>> provider6, Provider<Optional<SplitScreenController>> provider7, Provider<Optional<AppPairsController>> provider8, Provider<Optional<PipTouchHandler>> provider9, Provider<FullscreenTaskListener> provider10, Provider<Optional<FullscreenUnfoldController>> provider11, Provider<Optional<FreeformTaskListener>> provider12, Provider<Optional<RecentTasksController>> provider13, Provider<Transitions> provider14, Provider<StartingWindowController> provider15, Provider<ShellExecutor> provider16) {
        this.displayControllerProvider = provider;
        this.displayImeControllerProvider = provider2;
        this.displayInsetsControllerProvider = provider3;
        this.dragAndDropControllerProvider = provider4;
        this.shellTaskOrganizerProvider = provider5;
        this.bubblesOptionalProvider = provider6;
        this.splitScreenOptionalProvider = provider7;
        this.appPairsOptionalProvider = provider8;
        this.pipTouchHandlerOptionalProvider = provider9;
        this.fullscreenTaskListenerProvider = provider10;
        this.appUnfoldTransitionControllerProvider = provider11;
        this.freeformTaskListenerProvider = provider12;
        this.recentTasksOptionalProvider = provider13;
        this.transitionsProvider = provider14;
        this.startingWindowProvider = provider15;
        this.mainExecutorProvider = provider16;
    }

    public static WMShellBaseModule_ProvideShellInitImplFactory create(Provider<DisplayController> provider, Provider<DisplayImeController> provider2, Provider<DisplayInsetsController> provider3, Provider<DragAndDropController> provider4, Provider<ShellTaskOrganizer> provider5, Provider<Optional<BubbleController>> provider6, Provider<Optional<SplitScreenController>> provider7, Provider<Optional<AppPairsController>> provider8, Provider<Optional<PipTouchHandler>> provider9, Provider<FullscreenTaskListener> provider10, Provider<Optional<FullscreenUnfoldController>> provider11, Provider<Optional<FreeformTaskListener>> provider12, Provider<Optional<RecentTasksController>> provider13, Provider<Transitions> provider14, Provider<StartingWindowController> provider15, Provider<ShellExecutor> provider16) {
        return new WMShellBaseModule_ProvideShellInitImplFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ShellInitImpl(this.displayControllerProvider.mo144get(), this.displayImeControllerProvider.mo144get(), this.displayInsetsControllerProvider.mo144get(), this.dragAndDropControllerProvider.mo144get(), this.shellTaskOrganizerProvider.mo144get(), this.bubblesOptionalProvider.mo144get(), this.splitScreenOptionalProvider.mo144get(), this.appPairsOptionalProvider.mo144get(), this.pipTouchHandlerOptionalProvider.mo144get(), this.fullscreenTaskListenerProvider.mo144get(), this.appUnfoldTransitionControllerProvider.mo144get(), this.freeformTaskListenerProvider.mo144get(), this.recentTasksOptionalProvider.mo144get(), this.transitionsProvider.mo144get(), this.startingWindowProvider.mo144get(), this.mainExecutorProvider.mo144get());
    }
}
