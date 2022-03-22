package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.android.launcher3.icons.IconProvider;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.draganddrop.DragAndDropController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideDragAndDropControllerFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider displayControllerProvider;
    public final Provider iconProvider;
    public final Provider mainExecutorProvider;
    public final Provider uiEventLoggerProvider;

    public /* synthetic */ WMShellBaseModule_ProvideDragAndDropControllerFactory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
        this.iconProvider = provider4;
        this.mainExecutorProvider = provider5;
    }

    public static WMShellBaseModule_ProvideDragAndDropControllerFactory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new WMShellBaseModule_ProvideDragAndDropControllerFactory(provider, provider2, provider3, provider4, provider5, 1);
    }

    public static WMShellBaseModule_ProvideDragAndDropControllerFactory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new WMShellBaseModule_ProvideDragAndDropControllerFactory(provider, provider2, provider3, provider4, provider5, 0);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                Context context = (Context) this.contextProvider.mo144get();
                return new DragAndDropController((DisplayController) this.displayControllerProvider.mo144get(), (UiEventLogger) this.uiEventLoggerProvider.mo144get(), (IconProvider) this.iconProvider.mo144get(), (ShellExecutor) this.mainExecutorProvider.mo144get());
            default:
                return new RemoteInputCoordinator((DumpManager) this.contextProvider.mo144get(), (RemoteInputNotificationRebuilder) this.displayControllerProvider.mo144get(), (NotificationRemoteInputManager) this.uiEventLoggerProvider.mo144get(), (Handler) this.iconProvider.mo144get(), (SmartReplyController) this.mainExecutorProvider.mo144get());
        }
    }
}
