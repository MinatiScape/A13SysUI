package com.android.systemui.statusbar.notification.collection.notifcollection;
/* compiled from: NotifEvent.kt */
/* loaded from: classes.dex */
public final class RankingAppliedEvent extends NotifEvent {
    public RankingAppliedEvent() {
        super(0);
    }

    @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifEvent
    public final void dispatchToListener(NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onRankingApplied();
    }
}
