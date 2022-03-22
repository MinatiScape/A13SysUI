package com.android.systemui.statusbar.notification.row;

import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationGutsManager$$ExternalSyntheticLambda1 implements NotificationGuts.OnGutsClosedListener {
    public final /* synthetic */ NotificationGutsManager f$0;
    public final /* synthetic */ ExpandableNotificationRow f$1;
    public final /* synthetic */ NotificationEntry f$2;

    public /* synthetic */ NotificationGutsManager$$ExternalSyntheticLambda1(NotificationGutsManager notificationGutsManager, ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry) {
        this.f$0 = notificationGutsManager;
        this.f$1 = expandableNotificationRow;
        this.f$2 = notificationEntry;
    }

    public final void onGutsClosed(NotificationGuts notificationGuts) {
        NotificationGutsManager notificationGutsManager = this.f$0;
        ExpandableNotificationRow expandableNotificationRow = this.f$1;
        NotificationEntry notificationEntry = this.f$2;
        Objects.requireNonNull(notificationGutsManager);
        Objects.requireNonNull(expandableNotificationRow);
        expandableNotificationRow.updateContentAccessibilityImportanceForGuts(true);
        boolean z = false;
        expandableNotificationRow.mIsSnoozed = false;
        Objects.requireNonNull(notificationGuts);
        NotificationGuts.GutsContent gutsContent = notificationGuts.mGutsContent;
        if (gutsContent != null) {
            z = gutsContent.willBeRemoved();
        }
        if (!z && !expandableNotificationRow.mRemoved) {
            notificationGutsManager.mListContainer.onHeightChanged(expandableNotificationRow, true ^ ((StatusBarNotificationPresenter) notificationGutsManager.mPresenter).isPresenterFullyCollapsed());
        }
        if (notificationGutsManager.mNotificationGutsExposed == notificationGuts) {
            notificationGutsManager.mNotificationGutsExposed = null;
            notificationGutsManager.mGutsMenuItem = null;
        }
        NotifGutsViewListener notifGutsViewListener = notificationGutsManager.mGutsListener;
        if (notifGutsViewListener != null) {
            notifGutsViewListener.onGutsClose(notificationEntry);
        }
        Objects.requireNonNull(notificationEntry);
        String str = notificationEntry.mKey;
        if (str.equals(notificationGutsManager.mKeyToRemoveOnGutsClosed)) {
            notificationGutsManager.mKeyToRemoveOnGutsClosed = null;
            NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = notificationGutsManager.mNotificationLifetimeFinishedCallback;
            if (notificationSafeToRemoveCallback != null) {
                ((ScreenshotController$$ExternalSyntheticLambda3) notificationSafeToRemoveCallback).onSafeToRemove(str);
            }
        }
    }
}
