package com.android.systemui.statusbar.notification.collection.coalescer;

import java.util.Comparator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class GroupCoalescer$$ExternalSyntheticLambda0 implements Comparator {
    public static final /* synthetic */ GroupCoalescer$$ExternalSyntheticLambda0 INSTANCE = new GroupCoalescer$$ExternalSyntheticLambda0();

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        CoalescedEvent coalescedEvent = (CoalescedEvent) obj;
        CoalescedEvent coalescedEvent2 = (CoalescedEvent) obj2;
        Objects.requireNonNull(coalescedEvent2);
        boolean isGroupSummary = coalescedEvent2.sbn.getNotification().isGroupSummary();
        Objects.requireNonNull(coalescedEvent);
        int compare = Boolean.compare(isGroupSummary, coalescedEvent.sbn.getNotification().isGroupSummary());
        if (compare == 0) {
            return coalescedEvent.position - coalescedEvent2.position;
        }
        return compare;
    }
}
