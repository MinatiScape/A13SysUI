package com.android.systemui.screenrecord;

import android.app.NotificationManager;
import android.view.IWindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RecordingService_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider controllerProvider;
    public final Provider executorProvider;
    public final Provider keyguardDismissUtilProvider;
    public final Provider notificationManagerProvider;
    public final Provider uiEventLoggerProvider;
    public final Provider userContextTrackerProvider;

    public /* synthetic */ RecordingService_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, int i) {
        this.$r8$classId = i;
        this.controllerProvider = provider;
        this.executorProvider = provider2;
        this.uiEventLoggerProvider = provider3;
        this.notificationManagerProvider = provider4;
        this.userContextTrackerProvider = provider5;
        this.keyguardDismissUtilProvider = provider6;
    }

    public static RecordingService_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6) {
        return new RecordingService_Factory(provider, provider2, provider3, provider4, provider5, provider6, 0);
    }

    public static RecordingService_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6) {
        return new RecordingService_Factory(provider, provider2, provider3, provider4, provider5, provider6, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        DisplayImeController displayImeController;
        switch (this.$r8$classId) {
            case 0:
                return new RecordingService((RecordingController) this.controllerProvider.mo144get(), (Executor) this.executorProvider.mo144get(), (UiEventLogger) this.uiEventLoggerProvider.mo144get(), (NotificationManager) this.notificationManagerProvider.mo144get(), (UserContextProvider) this.userContextTrackerProvider.mo144get(), (KeyguardDismissUtil) this.keyguardDismissUtilProvider.mo144get());
            default:
                Optional optional = (Optional) this.controllerProvider.mo144get();
                IWindowManager iWindowManager = (IWindowManager) this.executorProvider.mo144get();
                DisplayController displayController = (DisplayController) this.uiEventLoggerProvider.mo144get();
                DisplayInsetsController displayInsetsController = (DisplayInsetsController) this.notificationManagerProvider.mo144get();
                ShellExecutor shellExecutor = (ShellExecutor) this.userContextTrackerProvider.mo144get();
                TransactionPool transactionPool = (TransactionPool) this.keyguardDismissUtilProvider.mo144get();
                if (optional.isPresent()) {
                    displayImeController = (DisplayImeController) optional.get();
                } else {
                    displayImeController = new DisplayImeController(iWindowManager, displayController, displayInsetsController, shellExecutor, transactionPool);
                }
                Objects.requireNonNull(displayImeController, "Cannot return null from a non-@Nullable @Provides method");
                return displayImeController;
        }
    }
}
