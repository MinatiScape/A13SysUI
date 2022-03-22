package com.android.systemui.statusbar.notification;

import android.app.AppGlobals;
import android.content.pm.IPackageManager;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.media.MediaDataManagerKt;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.systemui.util.Utils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationFilter {
    public final DebugModeFilterProvider mDebugNotificationFilter;
    public final ForegroundServiceController mForegroundServiceController;
    public final Boolean mIsMediaFlagEnabled;
    public final NotificationEntryManager.KeyguardEnvironment mKeyguardEnvironment;
    public final StatusBarStateController mStatusBarStateController;
    public final NotificationLockscreenUserManager mUserManager;

    @VisibleForTesting
    public static boolean showNotificationEvenIfUnprovisioned(IPackageManager iPackageManager, StatusBarNotification statusBarNotification) {
        try {
            if (iPackageManager.checkUidPermission("android.permission.NOTIFICATION_DURING_SETUP", statusBarNotification.getUid()) != 0 || !statusBarNotification.getNotification().extras.getBoolean("android.allowDuringSetup")) {
                return false;
            }
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public NotificationFilter(DebugModeFilterProvider debugModeFilterProvider, StatusBarStateController statusBarStateController, NotificationEntryManager.KeyguardEnvironment keyguardEnvironment, ForegroundServiceController foregroundServiceController, NotificationLockscreenUserManager notificationLockscreenUserManager, MediaFeatureFlag mediaFeatureFlag) {
        this.mDebugNotificationFilter = debugModeFilterProvider;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardEnvironment = keyguardEnvironment;
        this.mForegroundServiceController = foregroundServiceController;
        this.mUserManager = notificationLockscreenUserManager;
        Objects.requireNonNull(mediaFeatureFlag);
        this.mIsMediaFlagEnabled = Boolean.valueOf(Utils.useQsMediaPlayer(mediaFeatureFlag.context));
    }

    public final boolean shouldFilterOut(NotificationEntry notificationEntry) {
        boolean z;
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        DebugModeFilterProvider debugModeFilterProvider = this.mDebugNotificationFilter;
        Objects.requireNonNull(debugModeFilterProvider);
        if (debugModeFilterProvider.allowedPackages.isEmpty()) {
            z = false;
        } else {
            z = !debugModeFilterProvider.allowedPackages.contains(notificationEntry.mSbn.getPackageName());
        }
        if (z) {
            return true;
        }
        if ((!this.mKeyguardEnvironment.isDeviceProvisioned() && !showNotificationEvenIfUnprovisioned(AppGlobals.getPackageManager(), statusBarNotification)) || !this.mKeyguardEnvironment.isNotificationForCurrentProfiles(statusBarNotification)) {
            return true;
        }
        if (this.mUserManager.isLockscreenPublicMode(statusBarNotification.getUserId()) && (statusBarNotification.getNotification().visibility == -1 || this.mUserManager.shouldHideNotifications(statusBarNotification.getUserId()) || this.mUserManager.shouldHideNotifications(statusBarNotification.getKey()))) {
            return true;
        }
        if (this.mStatusBarStateController.isDozing() && notificationEntry.shouldSuppressVisualEffect(128)) {
            return true;
        }
        if ((!this.mStatusBarStateController.isDozing() && notificationEntry.shouldSuppressVisualEffect(256)) || notificationEntry.mRanking.isSuspended()) {
            return true;
        }
        Objects.requireNonNull(this.mForegroundServiceController);
        if (ForegroundServiceController.isDisclosureNotification(statusBarNotification) && !this.mForegroundServiceController.isDisclosureNeededForUser(statusBarNotification.getUserId())) {
            return true;
        }
        if (this.mIsMediaFlagEnabled.booleanValue()) {
            String[] strArr = MediaDataManagerKt.ART_URIS;
            if (statusBarNotification.getNotification().isMediaNotification()) {
                return true;
            }
        }
        return false;
    }
}
