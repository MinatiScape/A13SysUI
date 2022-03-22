package com.android.systemui.statusbar.notification.icon;

import android.app.Notification;
import android.app.Person;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import java.util.List;
import java.util.Objects;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: IconManager.kt */
/* loaded from: classes.dex */
public final class IconManager {
    public final IconBuilder iconBuilder;
    public final LauncherApps launcherApps;
    public final CommonNotifCollection notifCollection;
    public final IconManager$entryListener$1 entryListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.icon.IconManager$entryListener$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryCleanUp(NotificationEntry notificationEntry) {
            IconManager$sensitivityListener$1 iconManager$sensitivityListener$1 = IconManager.this.sensitivityListener;
            Objects.requireNonNull(notificationEntry);
            notificationEntry.mOnSensitivityChangedListeners.remove(iconManager$sensitivityListener$1);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryInit(NotificationEntry notificationEntry) {
            IconManager$sensitivityListener$1 iconManager$sensitivityListener$1 = IconManager.this.sensitivityListener;
            Objects.requireNonNull(notificationEntry);
            notificationEntry.mOnSensitivityChangedListeners.add(iconManager$sensitivityListener$1);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onRankingApplied() {
            for (NotificationEntry notificationEntry : IconManager.this.notifCollection.getAllNotifs()) {
                Objects.requireNonNull(IconManager.this);
                boolean isImportantConversation = IconManager.isImportantConversation(notificationEntry);
                IconPack iconPack = notificationEntry.mIcons;
                Objects.requireNonNull(iconPack);
                if (iconPack.mAreIconsAvailable) {
                    IconPack iconPack2 = notificationEntry.mIcons;
                    Objects.requireNonNull(iconPack2);
                    if (isImportantConversation != iconPack2.mIsImportantConversation) {
                        IconManager iconManager = IconManager.this;
                        Objects.requireNonNull(iconManager);
                        try {
                            iconManager.updateIcons(notificationEntry);
                        } catch (InflationException e) {
                            Log.e("IconManager", "Unable to update icon", e);
                        }
                    }
                }
                IconPack iconPack3 = notificationEntry.mIcons;
                Objects.requireNonNull(iconPack3);
                iconPack3.mIsImportantConversation = isImportantConversation;
            }
        }
    };
    public final IconManager$sensitivityListener$1 sensitivityListener = new NotificationEntry.OnSensitivityChangedListener() { // from class: com.android.systemui.statusbar.notification.icon.IconManager$sensitivityListener$1
        @Override // com.android.systemui.statusbar.notification.collection.NotificationEntry.OnSensitivityChangedListener
        public final void onSensitivityChanged(NotificationEntry notificationEntry) {
            IconManager iconManager = IconManager.this;
            Objects.requireNonNull(iconManager);
            try {
                iconManager.updateIcons(notificationEntry);
            } catch (InflationException e) {
                Log.e("IconManager", "Unable to update icon", e);
            }
        }
    };

    public final void updateIcons(NotificationEntry notificationEntry) throws InflationException {
        StatusBarIcon statusBarIcon;
        IconPack iconPack = notificationEntry.mIcons;
        Objects.requireNonNull(iconPack);
        if (iconPack.mAreIconsAvailable) {
            IconPack iconPack2 = notificationEntry.mIcons;
            Objects.requireNonNull(iconPack2);
            iconPack2.mSmallIconDescriptor = null;
            IconPack iconPack3 = notificationEntry.mIcons;
            Objects.requireNonNull(iconPack3);
            iconPack3.mPeopleAvatarDescriptor = null;
            StatusBarIcon iconDescriptor = getIconDescriptor(notificationEntry, false);
            if (notificationEntry.mSensitive) {
                statusBarIcon = getIconDescriptor(notificationEntry, true);
            } else {
                statusBarIcon = iconDescriptor;
            }
            Pair pair = new Pair(iconDescriptor, statusBarIcon);
            StatusBarIcon statusBarIcon2 = (StatusBarIcon) pair.component1();
            StatusBarIcon statusBarIcon3 = (StatusBarIcon) pair.component2();
            IconPack iconPack4 = notificationEntry.mIcons;
            Objects.requireNonNull(iconPack4);
            StatusBarIconView statusBarIconView = iconPack4.mStatusBarIcon;
            if (statusBarIconView != null) {
                statusBarIconView.setNotification(notificationEntry.mSbn);
                setIcon(notificationEntry, statusBarIcon2, statusBarIconView);
            }
            IconPack iconPack5 = notificationEntry.mIcons;
            Objects.requireNonNull(iconPack5);
            StatusBarIconView statusBarIconView2 = iconPack5.mShelfIcon;
            if (statusBarIconView2 != null) {
                statusBarIconView2.setNotification(notificationEntry.mSbn);
                setIcon(notificationEntry, statusBarIcon2, statusBarIconView2);
            }
            IconPack iconPack6 = notificationEntry.mIcons;
            Objects.requireNonNull(iconPack6);
            StatusBarIconView statusBarIconView3 = iconPack6.mAodIcon;
            if (statusBarIconView3 != null) {
                statusBarIconView3.setNotification(notificationEntry.mSbn);
                setIcon(notificationEntry, statusBarIcon3, statusBarIconView3);
            }
            IconPack iconPack7 = notificationEntry.mIcons;
            Objects.requireNonNull(iconPack7);
            StatusBarIconView statusBarIconView4 = iconPack7.mCenteredIcon;
            if (statusBarIconView4 != null) {
                statusBarIconView4.setNotification(notificationEntry.mSbn);
                setIcon(notificationEntry, statusBarIcon3, statusBarIconView4);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.notification.icon.IconManager$entryListener$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.notification.icon.IconManager$sensitivityListener$1] */
    public IconManager(CommonNotifCollection commonNotifCollection, LauncherApps launcherApps, IconBuilder iconBuilder) {
        this.notifCollection = commonNotifCollection;
        this.launcherApps = launcherApps;
        this.iconBuilder = iconBuilder;
    }

    public static boolean isImportantConversation(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        if (notificationEntry.mRanking.getChannel() == null || !notificationEntry.mRanking.getChannel().isImportantConversation()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x005e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void setIcon(com.android.systemui.statusbar.notification.collection.NotificationEntry r5, com.android.internal.statusbar.StatusBarIcon r6, com.android.systemui.statusbar.StatusBarIconView r7) throws com.android.systemui.statusbar.notification.InflationException {
        /*
            java.util.Objects.requireNonNull(r5)
            com.android.systemui.statusbar.notification.icon.IconPack r0 = r5.mIcons
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.StatusBarIconView r0 = r0.mShelfIcon
            r1 = 1
            r2 = 0
            if (r7 == r0) goto L_0x001a
            com.android.systemui.statusbar.notification.icon.IconPack r0 = r5.mIcons
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.StatusBarIconView r0 = r0.mAodIcon
            if (r7 != r0) goto L_0x0018
            goto L_0x001a
        L_0x0018:
            r0 = r2
            goto L_0x001b
        L_0x001a:
            r0 = r1
        L_0x001b:
            android.graphics.drawable.Icon r3 = r6.icon
            android.service.notification.StatusBarNotification r4 = r5.mSbn
            android.app.Notification r4 = r4.getNotification()
            android.graphics.drawable.Icon r4 = r4.getSmallIcon()
            boolean r3 = r3.equals(r4)
            boolean r4 = isImportantConversation(r5)
            if (r4 == 0) goto L_0x003b
            if (r3 != 0) goto L_0x003b
            if (r0 == 0) goto L_0x0039
            boolean r0 = r5.mSensitive
            if (r0 != 0) goto L_0x003b
        L_0x0039:
            r0 = r1
            goto L_0x003c
        L_0x003b:
            r0 = r2
        L_0x003c:
            boolean r3 = r7.mShowsConversation
            if (r3 == r0) goto L_0x0045
            r7.mShowsConversation = r0
            r7.updateIconColor()
        L_0x0045:
            r0 = 2131428107(0x7f0b030b, float:1.847785E38)
            int r5 = r5.targetSdk
            r3 = 21
            if (r5 >= r3) goto L_0x004f
            goto L_0x0050
        L_0x004f:
            r1 = r2
        L_0x0050:
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r1)
            r7.setTag(r0, r5)
            boolean r5 = r7.set(r6)
            if (r5 == 0) goto L_0x005e
            return
        L_0x005e:
            com.android.systemui.statusbar.notification.InflationException r5 = new com.android.systemui.statusbar.notification.InflationException
            java.lang.String r7 = "Couldn't create icon "
            java.lang.String r6 = kotlin.jvm.internal.Intrinsics.stringPlus(r7, r6)
            r5.<init>(r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.icon.IconManager.setIcon(com.android.systemui.statusbar.notification.collection.NotificationEntry, com.android.internal.statusbar.StatusBarIcon, com.android.systemui.statusbar.StatusBarIconView):void");
    }

    public final StatusBarIcon getIconDescriptor(NotificationEntry notificationEntry, boolean z) throws InflationException {
        boolean z2;
        Icon icon;
        Objects.requireNonNull(notificationEntry);
        Notification notification = notificationEntry.mSbn.getNotification();
        if (!isImportantConversation(notificationEntry) || z) {
            z2 = false;
        } else {
            z2 = true;
        }
        IconPack iconPack = notificationEntry.mIcons;
        Objects.requireNonNull(iconPack);
        StatusBarIcon statusBarIcon = iconPack.mPeopleAvatarDescriptor;
        IconPack iconPack2 = notificationEntry.mIcons;
        Objects.requireNonNull(iconPack2);
        StatusBarIcon statusBarIcon2 = iconPack2.mSmallIconDescriptor;
        if (z2 && statusBarIcon != null) {
            return statusBarIcon;
        }
        if (!z2 && statusBarIcon2 != null) {
            return statusBarIcon2;
        }
        if (z2) {
            ShortcutInfo conversationShortcutInfo = notificationEntry.mRanking.getConversationShortcutInfo();
            if (conversationShortcutInfo != null) {
                icon = this.launcherApps.getShortcutIcon(conversationShortcutInfo);
            } else {
                icon = null;
            }
            if (icon == null) {
                Bundle bundle = notificationEntry.mSbn.getNotification().extras;
                List<Notification.MessagingStyle.Message> messagesFromBundleArray = Notification.MessagingStyle.Message.getMessagesFromBundleArray(bundle.getParcelableArray("android.messages"));
                Person person = (Person) bundle.getParcelable("android.messagingUser");
                int size = messagesFromBundleArray.size() - 1;
                if (size >= 0) {
                    while (true) {
                        int i = size - 1;
                        Notification.MessagingStyle.Message message = messagesFromBundleArray.get(size);
                        Person senderPerson = message.getSenderPerson();
                        if (senderPerson != null && senderPerson != person) {
                            Person senderPerson2 = message.getSenderPerson();
                            Intrinsics.checkNotNull(senderPerson2);
                            icon = senderPerson2.getIcon();
                            break;
                        } else if (i < 0) {
                            break;
                        } else {
                            size = i;
                        }
                    }
                }
            }
            if (icon == null) {
                icon = notificationEntry.mSbn.getNotification().getLargeIcon();
            }
            if (icon == null) {
                icon = notificationEntry.mSbn.getNotification().getSmallIcon();
            }
            if (icon == null) {
                throw new InflationException(Intrinsics.stringPlus("No icon in notification from ", notificationEntry.mSbn.getPackageName()));
            }
        } else {
            icon = notification.getSmallIcon();
        }
        if (icon != null) {
            UserHandle user = notificationEntry.mSbn.getUser();
            String packageName = notificationEntry.mSbn.getPackageName();
            int i2 = notification.iconLevel;
            int i3 = notification.number;
            IconBuilder iconBuilder = this.iconBuilder;
            Objects.requireNonNull(iconBuilder);
            StatusBarIcon statusBarIcon3 = new StatusBarIcon(user, packageName, icon, i2, i3, StatusBarIconView.contentDescForNotification(iconBuilder.context, notification));
            if (isImportantConversation(notificationEntry)) {
                if (z2) {
                    IconPack iconPack3 = notificationEntry.mIcons;
                    Objects.requireNonNull(iconPack3);
                    iconPack3.mPeopleAvatarDescriptor = statusBarIcon3;
                } else {
                    IconPack iconPack4 = notificationEntry.mIcons;
                    Objects.requireNonNull(iconPack4);
                    iconPack4.mSmallIconDescriptor = statusBarIcon3;
                }
            }
            return statusBarIcon3;
        }
        throw new InflationException(Intrinsics.stringPlus("No icon in notification from ", notificationEntry.mSbn.getPackageName()));
    }
}
