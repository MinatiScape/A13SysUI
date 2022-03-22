package com.android.systemui.statusbar.notification.collection.coalescer;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CoalescedEvent.kt */
/* loaded from: classes.dex */
public final class CoalescedEvent {
    public EventBatch batch;
    public final String key;
    public int position;
    public NotificationListenerService.Ranking ranking;
    public StatusBarNotification sbn;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CoalescedEvent)) {
            return false;
        }
        CoalescedEvent coalescedEvent = (CoalescedEvent) obj;
        return Intrinsics.areEqual(this.key, coalescedEvent.key) && this.position == coalescedEvent.position && Intrinsics.areEqual(this.sbn, coalescedEvent.sbn) && Intrinsics.areEqual(this.ranking, coalescedEvent.ranking) && Intrinsics.areEqual(this.batch, coalescedEvent.batch);
    }

    public final int hashCode() {
        int i;
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.position, this.key.hashCode() * 31, 31);
        int hashCode = (this.ranking.hashCode() + ((this.sbn.hashCode() + m) * 31)) * 31;
        EventBatch eventBatch = this.batch;
        if (eventBatch == null) {
            i = 0;
        } else {
            i = eventBatch.hashCode();
        }
        return hashCode + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CoalescedEvent(key=");
        m.append(this.key);
        m.append(')');
        return m.toString();
    }

    public CoalescedEvent(String str, int i, StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking, EventBatch eventBatch) {
        this.key = str;
        this.position = i;
        this.sbn = statusBarNotification;
        this.ranking = ranking;
        this.batch = eventBatch;
    }
}
