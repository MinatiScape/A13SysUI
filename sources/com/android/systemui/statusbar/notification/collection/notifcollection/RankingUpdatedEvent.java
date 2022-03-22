package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.service.notification.NotificationListenerService;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class RankingUpdatedEvent extends NotifEvent {
    public final NotificationListenerService.RankingMap rankingMap;

    public RankingUpdatedEvent(NotificationListenerService.RankingMap rankingMap) {
        super(0);
        this.rankingMap = rankingMap;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof RankingUpdatedEvent) && Intrinsics.areEqual(this.rankingMap, ((RankingUpdatedEvent) obj).rankingMap);
    }

    public final int hashCode() {
        return this.rankingMap.hashCode();
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public final void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onRankingUpdate(this.rankingMap);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("RankingUpdatedEvent(rankingMap=");
        m.append(this.rankingMap);
        m.append(')');
        return m.toString();
    }
}
