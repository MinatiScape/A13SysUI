package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Objects;
import java.util.function.BiFunction;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationProcessor {
    public final ConversationNotificationManager conversationNotificationManager;
    public final LauncherApps launcherApps;

    public ConversationNotificationProcessor(LauncherApps launcherApps, ConversationNotificationManager conversationNotificationManager) {
        this.launcherApps = launcherApps;
        this.conversationNotificationManager = conversationNotificationManager;
    }

    public final void processNotification(final NotificationEntry notificationEntry, final Notification.Builder builder) {
        Notification.MessagingStyle messagingStyle;
        int i;
        Notification.Style style = builder.getStyle();
        if (style instanceof Notification.MessagingStyle) {
            messagingStyle = (Notification.MessagingStyle) style;
        } else {
            messagingStyle = null;
        }
        if (messagingStyle != null) {
            if (notificationEntry.mRanking.getChannel().isImportantConversation()) {
                i = 2;
            } else {
                i = 1;
            }
            messagingStyle.setConversationType(i);
            ShortcutInfo conversationShortcutInfo = notificationEntry.mRanking.getConversationShortcutInfo();
            if (conversationShortcutInfo != null) {
                messagingStyle.setShortcutIcon(this.launcherApps.getShortcutIcon(conversationShortcutInfo));
                CharSequence label = conversationShortcutInfo.getLabel();
                if (label != null) {
                    messagingStyle.setConversationTitle(label);
                }
            }
            final ConversationNotificationManager conversationNotificationManager = this.conversationNotificationManager;
            Objects.requireNonNull(conversationNotificationManager);
            ConversationNotificationManager.ConversationState compute = conversationNotificationManager.states.compute(notificationEntry.mKey, new BiFunction() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$getUnreadCount$1
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    boolean z;
                    String str = (String) obj;
                    ConversationNotificationManager.ConversationState conversationState = (ConversationNotificationManager.ConversationState) obj2;
                    int i2 = 1;
                    if (conversationState != null) {
                        ConversationNotificationManager conversationNotificationManager2 = conversationNotificationManager;
                        Notification.Builder builder2 = builder;
                        Objects.requireNonNull(conversationNotificationManager2);
                        Notification notification = conversationState.notification;
                        if ((notification.flags & 8) != 0) {
                            z = false;
                        } else {
                            z = Notification.areStyledNotificationsVisiblyDifferent(Notification.Builder.recoverBuilder(conversationNotificationManager2.context, notification), builder2);
                        }
                        if (z) {
                            i2 = 1 + conversationState.unreadCount;
                        } else {
                            i2 = conversationState.unreadCount;
                        }
                    }
                    NotificationEntry notificationEntry2 = NotificationEntry.this;
                    Objects.requireNonNull(notificationEntry2);
                    return new ConversationNotificationManager.ConversationState(i2, notificationEntry2.mSbn.getNotification());
                }
            });
            Intrinsics.checkNotNull(compute);
            messagingStyle.setUnreadMessageCount(compute.unreadCount);
        }
    }
}
