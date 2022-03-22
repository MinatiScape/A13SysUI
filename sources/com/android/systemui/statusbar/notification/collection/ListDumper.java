package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ListDumper {
    public static void dumpEntry(ListEntry listEntry, String str, String str2, StringBuilder sb, boolean z, boolean z2) {
        String str3;
        sb.append(str2);
        sb.append("[");
        sb.append(str);
        sb.append("] ");
        sb.append(listEntry.getKey());
        if (z) {
            sb.append(" (parent=");
            if (listEntry.getParent() != null) {
                GroupEntry parent = listEntry.getParent();
                Objects.requireNonNull(parent);
                str3 = parent.mKey;
            } else {
                str3 = null;
            }
            sb.append(str3);
            sb.append(")");
        }
        if (listEntry.getSection() != null) {
            sb.append(" section=");
            sb.append(listEntry.getSection().getLabel());
        }
        NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
        Objects.requireNonNull(representativeEntry);
        StringBuilder sb2 = new StringBuilder();
        if (!representativeEntry.mLifetimeExtenders.isEmpty()) {
            int size = representativeEntry.mLifetimeExtenders.size();
            String[] strArr = new String[size];
            for (int i = 0; i < size; i++) {
                strArr[i] = ((NotifLifetimeExtender) representativeEntry.mLifetimeExtenders.get(i)).getName();
            }
            sb2.append("lifetimeExtenders=");
            sb2.append(Arrays.toString(strArr));
            sb2.append(" ");
        }
        if (!representativeEntry.mDismissInterceptors.isEmpty()) {
            int size2 = representativeEntry.mDismissInterceptors.size();
            String[] strArr2 = new String[size2];
            for (int i2 = 0; i2 < size2; i2++) {
                ((NotifDismissInterceptor) representativeEntry.mDismissInterceptors.get(i2)).getName();
                strArr2[i2] = "BubbleCoordinator";
            }
            sb2.append("dismissInterceptors=");
            sb2.append(Arrays.toString(strArr2));
            sb2.append(" ");
        }
        ListAttachState listAttachState = representativeEntry.mAttachState;
        Objects.requireNonNull(listAttachState);
        if (listAttachState.excludingFilter != null) {
            sb2.append("filter=");
            ListAttachState listAttachState2 = representativeEntry.mAttachState;
            Objects.requireNonNull(listAttachState2);
            NotifFilter notifFilter = listAttachState2.excludingFilter;
            Objects.requireNonNull(notifFilter);
            sb2.append(notifFilter.mName);
            sb2.append(" ");
        }
        ListAttachState listAttachState3 = representativeEntry.mAttachState;
        Objects.requireNonNull(listAttachState3);
        if (listAttachState3.promoter != null) {
            sb2.append("promoter=");
            ListAttachState listAttachState4 = representativeEntry.mAttachState;
            Objects.requireNonNull(listAttachState4);
            NotifPromoter notifPromoter = listAttachState4.promoter;
            Objects.requireNonNull(notifPromoter);
            sb2.append(notifPromoter.mName);
            sb2.append(" ");
        }
        if (representativeEntry.mCancellationReason != -1) {
            sb2.append("cancellationReason=");
            sb2.append(representativeEntry.mCancellationReason);
            sb2.append(" ");
        }
        if (representativeEntry.mDismissState != NotificationEntry.DismissState.NOT_DISMISSED) {
            sb2.append("dismissState=");
            sb2.append(representativeEntry.mDismissState);
            sb2.append(" ");
        }
        ListAttachState listAttachState5 = representativeEntry.mAttachState;
        Objects.requireNonNull(listAttachState5);
        SuppressedAttachState suppressedAttachState = listAttachState5.suppressedChanges;
        Objects.requireNonNull(suppressedAttachState);
        if (suppressedAttachState.parent != null) {
            sb2.append("suppressedParent=");
            ListAttachState listAttachState6 = representativeEntry.mAttachState;
            Objects.requireNonNull(listAttachState6);
            SuppressedAttachState suppressedAttachState2 = listAttachState6.suppressedChanges;
            Objects.requireNonNull(suppressedAttachState2);
            GroupEntry groupEntry = suppressedAttachState2.parent;
            Objects.requireNonNull(groupEntry);
            sb2.append(groupEntry.mKey);
            sb2.append(" ");
        }
        ListAttachState listAttachState7 = representativeEntry.mAttachState;
        Objects.requireNonNull(listAttachState7);
        SuppressedAttachState suppressedAttachState3 = listAttachState7.suppressedChanges;
        Objects.requireNonNull(suppressedAttachState3);
        if (suppressedAttachState3.section != null) {
            sb2.append("suppressedSection=");
            ListAttachState listAttachState8 = representativeEntry.mAttachState;
            Objects.requireNonNull(listAttachState8);
            SuppressedAttachState suppressedAttachState4 = listAttachState8.suppressedChanges;
            Objects.requireNonNull(suppressedAttachState4);
            sb2.append(suppressedAttachState4.section);
            sb2.append(" ");
        }
        if (z2) {
            sb2.append("interacted=yes ");
        }
        String sb3 = sb2.toString();
        if (!sb3.isEmpty()) {
            sb.append("\n\t");
            sb.append(str2);
            sb.append(sb3);
        }
        sb.append("\n");
    }
}
