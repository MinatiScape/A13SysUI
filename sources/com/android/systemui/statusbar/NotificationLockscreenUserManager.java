package com.android.systemui.statusbar;

import android.content.pm.UserInfo;
import android.util.SparseArray;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1;
/* loaded from: classes.dex */
public interface NotificationLockscreenUserManager {

    /* loaded from: classes.dex */
    public interface KeyguardNotificationSuppressor {
        boolean shouldSuppressOnKeyguard(NotificationEntry notificationEntry);
    }

    /* loaded from: classes.dex */
    public interface UserChangedListener {
        default void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
        }

        default void onUserChanged(int i) {
        }
    }

    void addKeyguardNotificationSuppressor(KeyguardNotificationSuppressor keyguardNotificationSuppressor);

    void addUserChangedListener(UserChangedListener userChangedListener);

    int getCurrentUserId();

    boolean isAnyProfilePublicMode();

    boolean isCurrentProfile(int i);

    boolean isLockscreenPublicMode(int i);

    boolean needsRedaction(NotificationEntry notificationEntry);

    default boolean needsSeparateWorkChallenge(int i) {
        return false;
    }

    void removeUserChangedListener(NotificationVoiceReplyController$resetStateOnUserChange$listener$1 notificationVoiceReplyController$resetStateOnUserChange$listener$1);

    void setUpWithPresenter(NotificationPresenter notificationPresenter);

    boolean shouldAllowLockscreenRemoteInput();

    boolean shouldHideNotifications(int i);

    boolean shouldHideNotifications(String str);

    boolean shouldShowLockscreenNotifications();

    boolean shouldShowOnKeyguard(NotificationEntry notificationEntry);

    void updatePublicMode();

    boolean userAllowsNotificationsInPublic(int i);

    boolean userAllowsPrivateNotificationsInPublic(int i);
}
