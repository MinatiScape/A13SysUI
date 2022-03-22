package com.android.wm.shell.dagger;

import android.content.Context;
import android.util.Slog;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
import com.android.wm.shell.pip.phone.PipController;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellModule_ProvidePipFactory implements Factory<Optional<Pip>> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<OneHandedController>> oneHandedControllerProvider;
    public final Provider<PhonePipMenuController> phonePipMenuControllerProvider;
    public final Provider<PipAppOpsListener> pipAppOpsListenerProvider;
    public final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    public final Provider<PipBoundsState> pipBoundsStateProvider;
    public final Provider<PipMediaController> pipMediaControllerProvider;
    public final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;
    public final Provider<PipTouchHandler> pipTouchHandlerProvider;
    public final Provider<PipTransitionController> pipTransitionControllerProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    public final Provider<WindowManagerShellWrapper> windowManagerShellWrapperProvider;

    public static WMShellModule_ProvidePipFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<PipAppOpsListener> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PipBoundsState> provider5, Provider<PipMediaController> provider6, Provider<PhonePipMenuController> provider7, Provider<PipTaskOrganizer> provider8, Provider<PipTouchHandler> provider9, Provider<PipTransitionController> provider10, Provider<WindowManagerShellWrapper> provider11, Provider<TaskStackListenerImpl> provider12, Provider<Optional<OneHandedController>> provider13, Provider<ShellExecutor> provider14) {
        return new WMShellModule_ProvidePipFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        PipController.PipImpl pipImpl;
        Context context = this.contextProvider.mo144get();
        DisplayController displayController = this.displayControllerProvider.mo144get();
        PipAppOpsListener pipAppOpsListener = this.pipAppOpsListenerProvider.mo144get();
        PipBoundsAlgorithm pipBoundsAlgorithm = this.pipBoundsAlgorithmProvider.mo144get();
        PipBoundsState pipBoundsState = this.pipBoundsStateProvider.mo144get();
        PipMediaController pipMediaController = this.pipMediaControllerProvider.mo144get();
        PhonePipMenuController phonePipMenuController = this.phonePipMenuControllerProvider.mo144get();
        PipTaskOrganizer pipTaskOrganizer = this.pipTaskOrganizerProvider.mo144get();
        PipTouchHandler pipTouchHandler = this.pipTouchHandlerProvider.mo144get();
        PipTransitionController pipTransitionController = this.pipTransitionControllerProvider.mo144get();
        WindowManagerShellWrapper windowManagerShellWrapper = this.windowManagerShellWrapperProvider.mo144get();
        TaskStackListenerImpl taskStackListenerImpl = this.taskStackListenerProvider.mo144get();
        Optional<OneHandedController> optional = this.oneHandedControllerProvider.mo144get();
        ShellExecutor shellExecutor = this.mainExecutorProvider.mo144get();
        if (!context.getPackageManager().hasSystemFeature("android.software.picture_in_picture")) {
            Slog.w("PipController", "Device doesn't support Pip feature");
            pipImpl = null;
        } else {
            pipImpl = new PipController(context, displayController, pipAppOpsListener, pipBoundsAlgorithm, pipBoundsState, pipMediaController, phonePipMenuController, pipTaskOrganizer, pipTouchHandler, pipTransitionController, windowManagerShellWrapper, taskStackListenerImpl, optional, shellExecutor).mImpl;
        }
        Optional ofNullable = Optional.ofNullable(pipImpl);
        Objects.requireNonNull(ofNullable, "Cannot return null from a non-@Nullable @Provides method");
        return ofNullable;
    }

    public WMShellModule_ProvidePipFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<PipAppOpsListener> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PipBoundsState> provider5, Provider<PipMediaController> provider6, Provider<PhonePipMenuController> provider7, Provider<PipTaskOrganizer> provider8, Provider<PipTouchHandler> provider9, Provider<PipTransitionController> provider10, Provider<WindowManagerShellWrapper> provider11, Provider<TaskStackListenerImpl> provider12, Provider<Optional<OneHandedController>> provider13, Provider<ShellExecutor> provider14) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.pipAppOpsListenerProvider = provider3;
        this.pipBoundsAlgorithmProvider = provider4;
        this.pipBoundsStateProvider = provider5;
        this.pipMediaControllerProvider = provider6;
        this.phonePipMenuControllerProvider = provider7;
        this.pipTaskOrganizerProvider = provider8;
        this.pipTouchHandlerProvider = provider9;
        this.pipTransitionControllerProvider = provider10;
        this.windowManagerShellWrapperProvider = provider11;
        this.taskStackListenerProvider = provider12;
        this.oneHandedControllerProvider = provider13;
        this.mainExecutorProvider = provider14;
    }
}
