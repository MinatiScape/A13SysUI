package com.google.android.systemui.columbus;

import android.content.Context;
import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ColumbusStructuredDataManager_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bgExecutorProvider;
    public final Provider contextProvider;
    public final Provider userTrackerProvider;

    public /* synthetic */ ColumbusStructuredDataManager_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.userTrackerProvider = provider2;
        this.bgExecutorProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ColumbusStructuredDataManager((Context) this.contextProvider.mo144get(), (UserTracker) this.userTrackerProvider.mo144get(), (Executor) this.bgExecutorProvider.mo144get());
            case 1:
                return new MediaMuteAwaitConnectionManagerFactory((MediaFlags) this.contextProvider.mo144get(), (Context) this.userTrackerProvider.mo144get(), (Executor) this.bgExecutorProvider.mo144get());
            default:
                return new LowPriorityInflationHelper((NotificationGroupManagerLegacy) this.contextProvider.mo144get(), (RowContentBindStage) this.userTrackerProvider.mo144get(), (NotifPipelineFlags) this.bgExecutorProvider.mo144get());
        }
    }
}
