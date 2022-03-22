package com.android.systemui.statusbar.notification.collection.legacy;

import android.service.notification.NotificationListenerService;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes.dex */
public final class LegacyNotificationRankerStub implements LegacyNotificationRanker {
    public NotificationListenerService.RankingMap mRankingMap = new NotificationListenerService.RankingMap(new NotificationListenerService.Ranking[0]);
    public final Comparator<NotificationEntry> mEntryComparator = Comparator.comparingLong(LegacyNotificationRankerStub$$ExternalSyntheticLambda0.INSTANCE);

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public final boolean isNotificationForCurrentProfiles(NotificationEntry notificationEntry) {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public final List<NotificationEntry> updateRanking(NotificationListenerService.RankingMap rankingMap, Collection<NotificationEntry> collection, String str) {
        if (rankingMap != null) {
            this.mRankingMap = rankingMap;
        }
        ArrayList arrayList = new ArrayList(collection);
        arrayList.sort(this.mEntryComparator);
        return arrayList;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public final NotificationListenerService.RankingMap getRankingMap() {
        return this.mRankingMap;
    }
}
