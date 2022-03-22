package com.android.systemui.statusbar.notification.collection.coordinator;

import android.service.notification.StatusBarNotification;
import com.android.systemui.media.MediaDataManagerKt;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.util.Utils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaCoordinator implements Coordinator {
    public final Boolean mIsMediaFeatureEnabled;
    public final AnonymousClass1 mMediaFilter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator.1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            if (MediaCoordinator.this.mIsMediaFeatureEnabled.booleanValue()) {
                Objects.requireNonNull(notificationEntry);
                StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                String[] strArr = MediaDataManagerKt.ART_URIS;
                if (statusBarNotification.getNotification().isMediaNotification()) {
                    return true;
                }
            }
            return false;
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addPreGroupFilter(this.mMediaFilter);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator$1] */
    public MediaCoordinator(MediaFeatureFlag mediaFeatureFlag) {
        Objects.requireNonNull(mediaFeatureFlag);
        this.mIsMediaFeatureEnabled = Boolean.valueOf(Utils.useQsMediaPlayer(mediaFeatureFlag.context));
    }
}
