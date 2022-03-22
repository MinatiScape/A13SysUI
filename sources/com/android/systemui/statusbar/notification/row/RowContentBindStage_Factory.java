package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RowContentBindStage_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider binderProvider;
    public final Provider errorManagerProvider;
    public final Provider loggerProvider;

    public /* synthetic */ RowContentBindStage_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.binderProvider = provider;
        this.errorManagerProvider = provider2;
        this.loggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new RowContentBindStage((NotificationRowContentBinder) this.binderProvider.mo144get(), (NotifInflationErrorManager) this.errorManagerProvider.mo144get(), (RowContentBindStageLogger) this.loggerProvider.mo144get());
            default:
                return new BubbleCoordinator((Optional) this.binderProvider.mo144get(), (Optional) this.errorManagerProvider.mo144get(), (NotifCollection) this.loggerProvider.mo144get());
        }
    }
}
