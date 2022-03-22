package com.android.systemui.util;

import android.content.Context;
import android.media.AudioManager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.coordinator.RowAppearanceCoordinator;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RingerModeTrackerImpl_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider audioManagerProvider;
    public final Provider broadcastDispatcherProvider;
    public final Provider executorProvider;

    public /* synthetic */ RingerModeTrackerImpl_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.audioManagerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.executorProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new RingerModeTrackerImpl((AudioManager) this.audioManagerProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get(), (Executor) this.executorProvider.mo144get());
            case 1:
                return new StatusBarStateControllerImpl((UiEventLogger) this.audioManagerProvider.mo144get(), (DumpManager) this.broadcastDispatcherProvider.mo144get(), (InteractionJankMonitor) this.executorProvider.mo144get());
            case 2:
                return new RowAppearanceCoordinator((Context) this.audioManagerProvider.mo144get(), (AssistantFeedbackController) this.broadcastDispatcherProvider.mo144get(), (SectionClassifier) this.executorProvider.mo144get());
            default:
                return new NotificationGroupAlertTransferHelper((RowContentBindStage) this.audioManagerProvider.mo144get(), (StatusBarStateController) this.broadcastDispatcherProvider.mo144get(), (NotificationGroupManagerLegacy) this.executorProvider.mo144get());
        }
    }
}
