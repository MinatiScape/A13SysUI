package com.android.wm.shell.dagger;

import android.content.Context;
import android.content.om.IOverlayManager;
import android.os.Handler;
import android.os.ServiceManager;
import android.view.WindowManager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.BackgroundWindowManager;
import com.android.wm.shell.onehanded.OneHandedAccessibilityUtil;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer;
import com.android.wm.shell.onehanded.OneHandedSettingsUtil;
import com.android.wm.shell.onehanded.OneHandedState;
import com.android.wm.shell.onehanded.OneHandedTimeoutHandler;
import com.android.wm.shell.onehanded.OneHandedTouchHandler;
import com.android.wm.shell.onehanded.OneHandedTutorialHandler;
import com.android.wm.shell.onehanded.OneHandedUiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellModule_ProvideOneHandedControllerFactory implements Factory<OneHandedController> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<DisplayLayout> displayLayoutProvider;
    public final Provider<InteractionJankMonitor> jankMonitorProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public static WMShellModule_ProvideOneHandedControllerFactory create(Provider<Context> provider, Provider<WindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayLayout> provider4, Provider<TaskStackListenerImpl> provider5, Provider<UiEventLogger> provider6, Provider<InteractionJankMonitor> provider7, Provider<ShellExecutor> provider8, Provider<Handler> provider9) {
        return new WMShellModule_ProvideOneHandedControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Context context = this.contextProvider.mo144get();
        DisplayController displayController = this.displayControllerProvider.mo144get();
        DisplayLayout displayLayout = this.displayLayoutProvider.mo144get();
        TaskStackListenerImpl taskStackListenerImpl = this.taskStackListenerProvider.mo144get();
        UiEventLogger uiEventLogger = this.uiEventLoggerProvider.mo144get();
        InteractionJankMonitor interactionJankMonitor = this.jankMonitorProvider.mo144get();
        ShellExecutor shellExecutor = this.mainExecutorProvider.mo144get();
        Handler handler = this.mainHandlerProvider.mo144get();
        OneHandedSettingsUtil oneHandedSettingsUtil = new OneHandedSettingsUtil();
        OneHandedAccessibilityUtil oneHandedAccessibilityUtil = new OneHandedAccessibilityUtil(context);
        OneHandedTimeoutHandler oneHandedTimeoutHandler = new OneHandedTimeoutHandler(shellExecutor);
        OneHandedState oneHandedState = new OneHandedState();
        OneHandedTutorialHandler oneHandedTutorialHandler = new OneHandedTutorialHandler(context, oneHandedSettingsUtil, this.windowManagerProvider.mo144get(), new BackgroundWindowManager(context));
        OneHandedAnimationController oneHandedAnimationController = new OneHandedAnimationController(context);
        return new OneHandedController(context, displayController, new OneHandedDisplayAreaOrganizer(context, displayLayout, oneHandedAnimationController, oneHandedTutorialHandler, interactionJankMonitor, shellExecutor), new OneHandedTouchHandler(oneHandedTimeoutHandler, shellExecutor), oneHandedTutorialHandler, oneHandedSettingsUtil, oneHandedAccessibilityUtil, oneHandedTimeoutHandler, oneHandedState, interactionJankMonitor, new OneHandedUiEventLogger(uiEventLogger), IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay")), taskStackListenerImpl, shellExecutor, handler);
    }

    public WMShellModule_ProvideOneHandedControllerFactory(Provider<Context> provider, Provider<WindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayLayout> provider4, Provider<TaskStackListenerImpl> provider5, Provider<UiEventLogger> provider6, Provider<InteractionJankMonitor> provider7, Provider<ShellExecutor> provider8, Provider<Handler> provider9) {
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
        this.displayControllerProvider = provider3;
        this.displayLayoutProvider = provider4;
        this.taskStackListenerProvider = provider5;
        this.uiEventLoggerProvider = provider6;
        this.jankMonitorProvider = provider7;
        this.mainExecutorProvider = provider8;
        this.mainHandlerProvider = provider9;
    }
}
