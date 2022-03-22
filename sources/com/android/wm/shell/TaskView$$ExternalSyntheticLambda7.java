package com.android.wm.shell;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Binder;
import com.android.systemui.communal.CommunalHostViewController;
import com.android.systemui.communal.CommunalSource;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda7(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        CommunalSource communalSource;
        CommunalHostViewController communalHostViewController;
        ExpandableNotificationRow expandableNotificationRow;
        switch (this.$r8$classId) {
            case 0:
                TaskView taskView = (TaskView) this.f$0;
                Binder binder = (Binder) this.f$1;
                int i = TaskView.$r8$clinit;
                Objects.requireNonNull(taskView);
                ShellTaskOrganizer shellTaskOrganizer = taskView.mTaskOrganizer;
                Objects.requireNonNull(shellTaskOrganizer);
                synchronized (shellTaskOrganizer.mLock) {
                    shellTaskOrganizer.mLaunchCookieToListener.put(binder, taskView);
                }
                return;
            case 1:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                WeakReference<CommunalSource> weakReference = (WeakReference) this.f$1;
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                WeakReference<CommunalSource> weakReference2 = notificationPanelViewController.mCommunalSource;
                CommunalSource communalSource2 = null;
                if (weakReference2 != null) {
                    communalSource = weakReference2.get();
                } else {
                    communalSource = null;
                }
                if (communalSource != null) {
                    CommunalHostViewController communalHostViewController2 = notificationPanelViewController.mCommunalViewController;
                    Objects.requireNonNull(communalHostViewController2);
                    communalHostViewController2.mCurrentSource = null;
                    communalHostViewController2.showSource();
                }
                notificationPanelViewController.mCommunalSource = weakReference;
                if (weakReference != null) {
                    communalSource2 = weakReference.get();
                }
                if (!(communalSource2 == null || (communalHostViewController = notificationPanelViewController.mCommunalViewController) == null)) {
                    communalHostViewController.mCurrentSource = weakReference;
                    communalHostViewController.showSource();
                }
                notificationPanelViewController.updateKeyguardStatusViewAlignment(true);
                notificationPanelViewController.updateMaxDisplayedNotifications(true);
                return;
            case 2:
                StatusBarNotificationActivityStarter statusBarNotificationActivityStarter = (StatusBarNotificationActivityStarter) this.f$0;
                Objects.requireNonNull(statusBarNotificationActivityStarter);
                BubblesManager bubblesManager = statusBarNotificationActivityStarter.mBubblesManagerOptional.get();
                Objects.requireNonNull(bubblesManager);
                bubblesManager.mBubbles.expandStackAndSelectBubble(BubblesManager.notifToBubbleEntry((NotificationEntry) this.f$1));
                statusBarNotificationActivityStarter.mShadeController.collapsePanel();
                return;
            case 3:
                BubblesManager.AnonymousClass5 r0 = (BubblesManager.AnonymousClass5) this.f$0;
                Objects.requireNonNull(r0);
                NotificationEntry entry = BubblesManager.this.mCommonNotifCollection.getEntry((String) this.f$1);
                if (entry != null && (expandableNotificationRow = entry.row) != null) {
                    expandableNotificationRow.updateBubbleButton();
                    return;
                }
                return;
            case 4:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                Bubble bubble = (Bubble) this.f$1;
                Objects.requireNonNull(bubblesImpl);
                BubbleController bubbleController = BubbleController.this;
                if (bubble == null) {
                    Objects.requireNonNull(bubbleController);
                    return;
                } else if (bubbleController.mBubbleData.hasBubbleInStackWithKey(bubble.mKey)) {
                    bubbleController.mBubbleData.setSelectedBubble(bubble);
                    bubbleController.mBubbleData.setExpanded(true);
                    return;
                } else if (bubbleController.mBubbleData.hasOverflowBubbleWithKey(bubble.mKey)) {
                    bubbleController.promoteBubbleFromOverflow(bubble);
                    return;
                } else {
                    return;
                }
            default:
                LegacySplitScreenTransitions legacySplitScreenTransitions = (LegacySplitScreenTransitions) this.f$0;
                Objects.requireNonNull(legacySplitScreenTransitions);
                legacySplitScreenTransitions.mAnimations.remove((ValueAnimator) this.f$1);
                legacySplitScreenTransitions.onFinish();
                return;
        }
    }
}
