package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.NotificationChannel;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ConversationCoordinator.kt */
/* loaded from: classes.dex */
public final class ConversationCoordinator implements Coordinator {
    public final ConversationCoordinator$notificationPromoter$1 notificationPromoter = new NotifPromoter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator$notificationPromoter$1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter
        public final boolean shouldPromoteToTopLevel(NotificationEntry notificationEntry) {
            NotificationChannel channel = notificationEntry.getChannel();
            if (channel != null && channel.isImportantConversation()) {
                return true;
            }
            return false;
        }
    };
    public final PeopleNotificationIdentifier peopleNotificationIdentifier;
    public final ConversationCoordinator$sectioner$1 sectioner;

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addPromoter(this.notificationPromoter);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator$notificationPromoter$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator$sectioner$1] */
    public ConversationCoordinator(PeopleNotificationIdentifier peopleNotificationIdentifier, final NodeController nodeController) {
        this.peopleNotificationIdentifier = peopleNotificationIdentifier;
        this.sectioner = new NotifSectioner() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator$sectioner$1
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
            public final NodeController getHeaderNodeController() {
                return null;
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super("People", 4);
            }

            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
            public final NotifComparator getComparator() {
                final ConversationCoordinator conversationCoordinator = ConversationCoordinator.this;
                return new NotifComparator() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator$sectioner$1$getComparator$1
                    {
                        super("People");
                    }

                    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator
                    public final int compare(ListEntry listEntry, ListEntry listEntry2) {
                        return Intrinsics.compare(ConversationCoordinator.this.getPeopleType(listEntry2), ConversationCoordinator.this.getPeopleType(listEntry));
                    }
                };
            }

            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
            public final boolean isInSection(ListEntry listEntry) {
                ConversationCoordinator conversationCoordinator = ConversationCoordinator.this;
                Objects.requireNonNull(conversationCoordinator);
                if (conversationCoordinator.getPeopleType(listEntry) != 0) {
                    return true;
                }
                return false;
            }
        };
    }

    public final int getPeopleType(ListEntry listEntry) {
        NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
        if (representativeEntry == null) {
            return 0;
        }
        return this.peopleNotificationIdentifier.getPeopleNotificationType(representativeEntry);
    }
}
