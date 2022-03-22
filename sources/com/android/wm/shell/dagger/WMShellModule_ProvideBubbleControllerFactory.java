package com.android.wm.shell.dagger;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.os.Handler;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.BubbleData;
import com.android.wm.shell.bubbles.BubbleDataRepository;
import com.android.wm.shell.bubbles.BubbleLogger;
import com.android.wm.shell.bubbles.BubblePositioner;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellModule_ProvideBubbleControllerFactory implements Factory<BubbleController> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<FloatingContentCoordinator> floatingContentCoordinatorProvider;
    public final Provider<LauncherApps> launcherAppsProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<Optional<OneHandedController>> oneHandedOptionalProvider;
    public final Provider<ShellTaskOrganizer> organizerProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;
    public final Provider<SyncTransactionQueue> syncQueueProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    public final Provider<TaskViewTransitions> taskViewTransitionsProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<WindowManager> windowManagerProvider;
    public final Provider<WindowManagerShellWrapper> windowManagerShellWrapperProvider;

    public static WMShellModule_ProvideBubbleControllerFactory create(Provider<Context> provider, Provider<FloatingContentCoordinator> provider2, Provider<IStatusBarService> provider3, Provider<WindowManager> provider4, Provider<WindowManagerShellWrapper> provider5, Provider<LauncherApps> provider6, Provider<TaskStackListenerImpl> provider7, Provider<UiEventLogger> provider8, Provider<ShellTaskOrganizer> provider9, Provider<DisplayController> provider10, Provider<Optional<OneHandedController>> provider11, Provider<ShellExecutor> provider12, Provider<Handler> provider13, Provider<TaskViewTransitions> provider14, Provider<SyncTransactionQueue> provider15) {
        return new WMShellModule_ProvideBubbleControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Context context = this.contextProvider.mo144get();
        FloatingContentCoordinator floatingContentCoordinator = this.floatingContentCoordinatorProvider.mo144get();
        IStatusBarService iStatusBarService = this.statusBarServiceProvider.mo144get();
        WindowManager windowManager = this.windowManagerProvider.mo144get();
        WindowManagerShellWrapper windowManagerShellWrapper = this.windowManagerShellWrapperProvider.mo144get();
        LauncherApps launcherApps = this.launcherAppsProvider.mo144get();
        TaskStackListenerImpl taskStackListenerImpl = this.taskStackListenerProvider.mo144get();
        ShellTaskOrganizer shellTaskOrganizer = this.organizerProvider.mo144get();
        DisplayController displayController = this.displayControllerProvider.mo144get();
        Optional<OneHandedController> optional = this.oneHandedOptionalProvider.mo144get();
        ShellExecutor shellExecutor = this.mainExecutorProvider.mo144get();
        SyncTransactionQueue syncTransactionQueue = this.syncQueueProvider.mo144get();
        BubbleLogger bubbleLogger = new BubbleLogger(this.uiEventLoggerProvider.mo144get());
        BubblePositioner bubblePositioner = new BubblePositioner(context, windowManager);
        return new BubbleController(context, new BubbleData(context, bubbleLogger, bubblePositioner, shellExecutor), null, floatingContentCoordinator, new BubbleDataRepository(context, launcherApps, shellExecutor), iStatusBarService, windowManager, windowManagerShellWrapper, launcherApps, bubbleLogger, taskStackListenerImpl, shellTaskOrganizer, bubblePositioner, displayController, optional, shellExecutor, this.mainHandlerProvider.mo144get(), this.taskViewTransitionsProvider.mo144get(), syncTransactionQueue);
    }

    public WMShellModule_ProvideBubbleControllerFactory(Provider<Context> provider, Provider<FloatingContentCoordinator> provider2, Provider<IStatusBarService> provider3, Provider<WindowManager> provider4, Provider<WindowManagerShellWrapper> provider5, Provider<LauncherApps> provider6, Provider<TaskStackListenerImpl> provider7, Provider<UiEventLogger> provider8, Provider<ShellTaskOrganizer> provider9, Provider<DisplayController> provider10, Provider<Optional<OneHandedController>> provider11, Provider<ShellExecutor> provider12, Provider<Handler> provider13, Provider<TaskViewTransitions> provider14, Provider<SyncTransactionQueue> provider15) {
        this.contextProvider = provider;
        this.floatingContentCoordinatorProvider = provider2;
        this.statusBarServiceProvider = provider3;
        this.windowManagerProvider = provider4;
        this.windowManagerShellWrapperProvider = provider5;
        this.launcherAppsProvider = provider6;
        this.taskStackListenerProvider = provider7;
        this.uiEventLoggerProvider = provider8;
        this.organizerProvider = provider9;
        this.displayControllerProvider = provider10;
        this.oneHandedOptionalProvider = provider11;
        this.mainExecutorProvider = provider12;
        this.mainHandlerProvider = provider13;
        this.taskViewTransitionsProvider = provider14;
        this.syncQueueProvider = provider15;
    }
}
