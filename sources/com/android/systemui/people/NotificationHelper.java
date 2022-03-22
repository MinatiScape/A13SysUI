package com.android.systemui.people;

import android.app.Notification;
import android.app.Person;
import android.os.Bundle;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationHelper {
    public static AnonymousClass1 notificationEntryComparator = new Comparator<NotificationEntry>() { // from class: com.android.systemui.people.NotificationHelper.1
        @Override // java.util.Comparator
        public final int compare(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
            NotificationEntry notificationEntry3 = notificationEntry;
            NotificationEntry notificationEntry4 = notificationEntry2;
            Objects.requireNonNull(notificationEntry3);
            Notification notification = notificationEntry3.mSbn.getNotification();
            Objects.requireNonNull(notificationEntry4);
            Notification notification2 = notificationEntry4.mSbn.getNotification();
            boolean isMissedCall = NotificationHelper.isMissedCall(notification);
            boolean isMissedCall2 = NotificationHelper.isMissedCall(notification2);
            if (isMissedCall && !isMissedCall2) {
                return -1;
            }
            if (isMissedCall || !isMissedCall2) {
                List<Notification.MessagingStyle.Message> messagingStyleMessages = NotificationHelper.getMessagingStyleMessages(notification);
                List<Notification.MessagingStyle.Message> messagingStyleMessages2 = NotificationHelper.getMessagingStyleMessages(notification2);
                if (messagingStyleMessages != null && messagingStyleMessages2 != null) {
                    return (int) (messagingStyleMessages2.get(0).getTimestamp() - messagingStyleMessages.get(0).getTimestamp());
                }
                if (messagingStyleMessages != null) {
                    if (messagingStyleMessages2 == null) {
                        return -1;
                    }
                    return (int) (notification2.when - notification.when);
                }
            }
            return 1;
        }
    };

    @VisibleForTesting
    public static List<Notification.MessagingStyle.Message> getMessagingStyleMessages(Notification notification) {
        Bundle bundle;
        if (!(notification == null || !notification.isStyle(Notification.MessagingStyle.class) || (bundle = notification.extras) == null)) {
            Parcelable[] parcelableArray = bundle.getParcelableArray("android.messages");
            if (!ArrayUtils.isEmpty(parcelableArray)) {
                List<Notification.MessagingStyle.Message> messagesFromBundleArray = Notification.MessagingStyle.Message.getMessagesFromBundleArray(parcelableArray);
                messagesFromBundleArray.sort(Collections.reverseOrder(Comparator.comparing(NotificationHelper$$ExternalSyntheticLambda0.INSTANCE)));
                return messagesFromBundleArray;
            }
        }
        return null;
    }

    public static boolean isMissedCallOrHasContent(NotificationEntry notificationEntry) {
        boolean z;
        boolean z2;
        List<Notification.MessagingStyle.Message> messagingStyleMessages;
        if (notificationEntry == null || notificationEntry.mSbn.getNotification() == null || !isMissedCall(notificationEntry.mSbn.getNotification())) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            return true;
        }
        if (notificationEntry == null || (messagingStyleMessages = getMessagingStyleMessages(notificationEntry.mSbn.getNotification())) == null || messagingStyleMessages.isEmpty()) {
            z2 = false;
        } else {
            z2 = true;
        }
        return z2;
    }

    public static boolean isMissedCall(Notification notification) {
        if (notification == null || !Objects.equals(notification.category, "missed_call")) {
            return false;
        }
        return true;
    }

    public static String getContactUri(StatusBarNotification statusBarNotification) {
        Person senderPerson;
        String uri;
        ArrayList parcelableArrayList = statusBarNotification.getNotification().extras.getParcelableArrayList("android.people.list");
        if (parcelableArrayList != null && parcelableArrayList.get(0) != null && (uri = ((Person) parcelableArrayList.get(0)).getUri()) != null && !uri.isEmpty()) {
            return uri;
        }
        List<Notification.MessagingStyle.Message> messagingStyleMessages = getMessagingStyleMessages(statusBarNotification.getNotification());
        if (messagingStyleMessages == null || messagingStyleMessages.isEmpty() || (senderPerson = messagingStyleMessages.get(0).getSenderPerson()) == null || senderPerson.getUri() == null || senderPerson.getUri().isEmpty()) {
            return null;
        }
        return senderPerson.getUri();
    }
}
