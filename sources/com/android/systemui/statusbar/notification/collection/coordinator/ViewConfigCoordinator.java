package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingMessage;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.Objects;
/* compiled from: ViewConfigCoordinator.kt */
/* loaded from: classes.dex */
public final class ViewConfigCoordinator implements Coordinator, NotificationLockscreenUserManager.UserChangedListener, ConfigurationController.ConfigurationListener {
    public final ConfigurationController mConfigurationController;
    public boolean mDispatchUiModeChangeOnUserSwitched;
    public final NotificationGutsManager mGutsManager;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final NotificationLockscreenUserManagerImpl mLockscreenUserManager;
    public NotifPipeline mPipeline;
    public boolean mReinflateNotificationsOnUserSwitched;

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        this.mPipeline = notifPipeline;
        if (notifPipeline.isNewPipelineEnabled) {
            this.mLockscreenUserManager.addUserChangedListener(this);
            this.mConfigurationController.addCallback(this);
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onUiModeChanged() {
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        if (!keyguardUpdateMonitor.mSwitchingUser) {
            updateNotificationsOnUiModeChanged();
        } else {
            this.mDispatchUiModeChangeOnUserSwitched = true;
        }
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager.UserChangedListener
    public final void onUserChanged(int i) {
        if (this.mReinflateNotificationsOnUserSwitched) {
            updateNotificationsOnDensityOrFontScaleChanged();
            this.mReinflateNotificationsOnUserSwitched = false;
        }
        if (this.mDispatchUiModeChangeOnUserSwitched) {
            updateNotificationsOnUiModeChanged();
            this.mDispatchUiModeChangeOnUserSwitched = false;
        }
    }

    public final void updateNotificationsOnDensityOrFontScaleChanged() {
        NotificationGuts notificationGuts;
        NotifPipeline notifPipeline = this.mPipeline;
        if (notifPipeline != null) {
            for (NotificationEntry notificationEntry : notifPipeline.getAllNotifs()) {
                Objects.requireNonNull(notificationEntry);
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                if (expandableNotificationRow != null) {
                    expandableNotificationRow.onDensityOrFontScaleChanged();
                }
                if (notificationEntry.areGutsExposed()) {
                    NotificationGutsManager notificationGutsManager = this.mGutsManager;
                    Objects.requireNonNull(notificationGutsManager);
                    ExpandableNotificationRow expandableNotificationRow2 = notificationEntry.row;
                    if (expandableNotificationRow2 != null) {
                        notificationGuts = expandableNotificationRow2.mGuts;
                    } else {
                        notificationGuts = null;
                    }
                    notificationGutsManager.mNotificationGutsExposed = notificationGuts;
                    Objects.requireNonNull(expandableNotificationRow2);
                    if (expandableNotificationRow2.mGuts == null) {
                        expandableNotificationRow2.mGutsStub.inflate();
                    }
                    notificationGutsManager.bindGuts(expandableNotificationRow2, notificationGutsManager.mGutsMenuItem);
                }
            }
        }
    }

    public final void updateNotificationsOnUiModeChanged() {
        NotifPipeline notifPipeline = this.mPipeline;
        if (notifPipeline != null) {
            for (NotificationEntry notificationEntry : notifPipeline.getAllNotifs()) {
                Objects.requireNonNull(notificationEntry);
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                if (expandableNotificationRow != null) {
                    expandableNotificationRow.onUiModeChanged();
                }
            }
        }
    }

    public ViewConfigCoordinator(ConfigurationController configurationController, NotificationLockscreenUserManagerImpl notificationLockscreenUserManagerImpl, NotificationGutsManager notificationGutsManager, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mConfigurationController = configurationController;
        this.mLockscreenUserManager = notificationLockscreenUserManagerImpl;
        this.mGutsManager = notificationGutsManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onDensityOrFontScaleChanged() {
        MessagingMessage.dropCache();
        MessagingGroup.dropCache();
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        if (!keyguardUpdateMonitor.mSwitchingUser) {
            updateNotificationsOnDensityOrFontScaleChanged();
        } else {
            this.mReinflateNotificationsOnUserSwitched = true;
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onThemeChanged() {
        onDensityOrFontScaleChanged();
    }
}
