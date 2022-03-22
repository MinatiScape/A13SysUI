package com.android.systemui.statusbar.notification.people;

import android.app.NotificationChannel;
import android.service.notification.NotificationListenerService;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.TransformingSequence;
/* compiled from: PeopleNotificationIdentifier.kt */
/* loaded from: classes.dex */
public final class PeopleNotificationIdentifierImpl implements PeopleNotificationIdentifier {
    public final GroupMembershipManager groupManager;
    public final NotificationPersonExtractor personExtractor;

    @Override // com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier
    public final int getPeopleNotificationType(NotificationEntry notificationEntry) {
        TransformingSequence transformingSequence;
        NotificationListenerService.Ranking ranking = notificationEntry.mRanking;
        int i = 1;
        int i2 = 0;
        if (!ranking.isConversation()) {
            i = 0;
        } else if (ranking.getConversationShortcutInfo() != null) {
            NotificationChannel channel = ranking.getChannel();
            if (channel == null || !channel.isImportantConversation()) {
                i = 0;
            }
            if (i != 0) {
                i = 3;
            } else {
                i = 2;
            }
        }
        if (i == 3) {
            return 3;
        }
        int max = Math.max(i, this.personExtractor.isPersonNotification(notificationEntry.mSbn) ? 1 : 0);
        if (max == 3) {
            return 3;
        }
        if (this.groupManager.isGroupSummary(notificationEntry)) {
            List<NotificationEntry> children = this.groupManager.getChildren(notificationEntry);
            if (children == null) {
                transformingSequence = null;
            } else {
                transformingSequence = new TransformingSequence(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(children), new PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1(this));
            }
            if (transformingSequence != null) {
                Iterator it = transformingSequence.sequence.iterator();
                while (it.hasNext() && (i2 = Math.max(i2, ((Number) transformingSequence.transformer.invoke(it.next())).intValue())) != 3) {
                }
            }
        }
        return Math.max(max, i2);
    }

    public PeopleNotificationIdentifierImpl(NotificationPersonExtractor notificationPersonExtractor, GroupMembershipManager groupMembershipManager) {
        this.personExtractor = notificationPersonExtractor;
        this.groupManager = groupMembershipManager;
    }

    @Override // com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier
    public final int compareTo(int i, int i2) {
        return Intrinsics.compare(i2, i);
    }
}
