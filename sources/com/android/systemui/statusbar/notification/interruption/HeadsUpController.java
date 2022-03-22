package com.android.systemui.statusbar.notification.interruption;

import android.app.Notification;
import android.util.Log;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HeadsUpController {
    public final HeadsUpManager mHeadsUpManager;
    public final HeadsUpViewBinder mHeadsUpViewBinder;
    public final NotificationInterruptStateProvider mInterruptStateProvider;
    public final NotificationListener mNotificationListener;
    public final NotificationRemoteInputManager mRemoteInputManager;
    public final StatusBarStateController mStatusBarStateController;
    public final VisualStabilityManager mVisualStabilityManager;
    public AnonymousClass1 mCollectionListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.interruption.HeadsUpController.1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryAdded(NotificationEntry notificationEntry) {
            if (HeadsUpController.this.mInterruptStateProvider.shouldHeadsUp(notificationEntry)) {
                final HeadsUpController headsUpController = HeadsUpController.this;
                headsUpController.mHeadsUpViewBinder.bindHeadsUpView(notificationEntry, new NotifBindPipeline.BindCallback() { // from class: com.android.systemui.statusbar.notification.interruption.HeadsUpController$1$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
                    public final void onBindFinished(NotificationEntry notificationEntry2) {
                        HeadsUpController headsUpController2 = HeadsUpController.this;
                        Objects.requireNonNull(headsUpController2);
                        headsUpController2.mHeadsUpManager.showNotification(notificationEntry2);
                        if (!headsUpController2.mStatusBarStateController.isDozing()) {
                            try {
                                headsUpController2.mNotificationListener.setNotificationsShown(new String[]{notificationEntry2.mSbn.getKey()});
                            } catch (RuntimeException e) {
                                Log.d("HeadsUpBindController", "failed setNotificationsShown: ", e);
                            }
                        }
                    }
                });
            }
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryCleanUp(NotificationEntry notificationEntry) {
            HeadsUpController.this.mHeadsUpViewBinder.abortBindCallback(notificationEntry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
            boolean z;
            HeadsUpController headsUpController = HeadsUpController.this;
            Objects.requireNonNull(headsUpController);
            Objects.requireNonNull(notificationEntry);
            String str = notificationEntry.mKey;
            if (headsUpController.mHeadsUpManager.isAlerting(str)) {
                if (!headsUpController.mRemoteInputManager.isSpinning(str) || NotificationRemoteInputManager.FORCE_REMOTE_INPUT_HISTORY) {
                    VisualStabilityManager visualStabilityManager = headsUpController.mVisualStabilityManager;
                    Objects.requireNonNull(visualStabilityManager);
                    if (visualStabilityManager.mReorderingAllowed) {
                        z = false;
                        headsUpController.mHeadsUpManager.removeNotification(str, z);
                    }
                }
                z = true;
                headsUpController.mHeadsUpManager.removeNotification(str, z);
            }
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryUpdated(NotificationEntry notificationEntry) {
            boolean z;
            HeadsUpController headsUpController = HeadsUpController.this;
            Objects.requireNonNull(headsUpController);
            Objects.requireNonNull(notificationEntry);
            Notification notification = notificationEntry.mSbn.getNotification();
            if (!notificationEntry.interruption || (notification.flags & 8) == 0) {
                z = true;
            } else {
                z = false;
            }
            boolean shouldHeadsUp = headsUpController.mInterruptStateProvider.shouldHeadsUp(notificationEntry);
            if (headsUpController.mHeadsUpManager.isAlerting(notificationEntry.mKey)) {
                if (shouldHeadsUp) {
                    headsUpController.mHeadsUpManager.updateNotification(notificationEntry.mKey, z);
                } else {
                    headsUpController.mHeadsUpManager.removeNotification(notificationEntry.mKey, false);
                }
            } else if (shouldHeadsUp && z) {
                HeadsUpViewBinder headsUpViewBinder = headsUpController.mHeadsUpViewBinder;
                final HeadsUpManager headsUpManager = headsUpController.mHeadsUpManager;
                Objects.requireNonNull(headsUpManager);
                headsUpViewBinder.bindHeadsUpView(notificationEntry, new NotifBindPipeline.BindCallback() { // from class: com.android.systemui.statusbar.notification.interruption.HeadsUpController$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
                    public final void onBindFinished(NotificationEntry notificationEntry2) {
                        HeadsUpManager.this.showNotification(notificationEntry2);
                    }
                });
            }
        }
    };
    public AnonymousClass2 mOnHeadsUpChangedListener = new OnHeadsUpChangedListener() { // from class: com.android.systemui.statusbar.notification.interruption.HeadsUpController.2
        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public final void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
            if (!z) {
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                Objects.requireNonNull(expandableNotificationRow);
                if (!expandableNotificationRow.mRemoved) {
                    HeadsUpController.this.mHeadsUpViewBinder.unbindHeadsUpView(notificationEntry);
                }
            }
        }
    };

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.notification.interruption.HeadsUpController$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.notification.interruption.HeadsUpController$2] */
    public HeadsUpController(HeadsUpViewBinder headsUpViewBinder, NotificationInterruptStateProvider notificationInterruptStateProvider, HeadsUpManager headsUpManager, NotificationRemoteInputManager notificationRemoteInputManager, StatusBarStateController statusBarStateController, VisualStabilityManager visualStabilityManager, NotificationListener notificationListener) {
        this.mHeadsUpViewBinder = headsUpViewBinder;
        this.mHeadsUpManager = headsUpManager;
        this.mInterruptStateProvider = notificationInterruptStateProvider;
        this.mRemoteInputManager = notificationRemoteInputManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mVisualStabilityManager = visualStabilityManager;
        this.mNotificationListener = notificationListener;
    }
}
