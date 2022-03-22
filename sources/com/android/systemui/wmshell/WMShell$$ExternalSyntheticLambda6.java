package com.android.systemui.wmshell;

import android.content.Intent;
import android.graphics.Rect;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShell$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WMShell$$ExternalSyntheticLambda6(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((WMShell) this.f$0).initPip((Pip) obj);
                return;
            case 1:
                NotificationMediaManager.AnonymousClass3 r3 = (NotificationMediaManager.AnonymousClass3) this.f$0;
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                Objects.requireNonNull(r3);
                NotificationMediaManager notificationMediaManager = NotificationMediaManager.this;
                notificationMediaManager.mNotifCollection.dismissNotification(notificationEntry, new DismissedByUserStats(3, notificationMediaManager.mVisibilityProvider.obtain(notificationEntry, true)));
                return;
            case 2:
                KeyguardBottomAreaView keyguardBottomAreaView = (KeyguardBottomAreaView) this.f$0;
                Intent intent = KeyguardBottomAreaView.PHONE_INTENT;
                Objects.requireNonNull(keyguardBottomAreaView);
                ((ControlsListingController) obj).addCallback(keyguardBottomAreaView.mListingCallback);
                return;
            case 3:
                PipAccessibilityInteractionConnection pipAccessibilityInteractionConnection = (PipAccessibilityInteractionConnection) this.f$0;
                Rect rect = (Rect) obj;
                Objects.requireNonNull(pipAccessibilityInteractionConnection);
                pipAccessibilityInteractionConnection.mMotionHelper.synchronizePinnedStackBounds();
                pipAccessibilityInteractionConnection.mUpdateMovementBoundCallback.run();
                return;
            default:
                StartingWindowController.IStartingWindowImpl iStartingWindowImpl = (StartingWindowController.IStartingWindowImpl) this.f$0;
                StartingWindowController startingWindowController = (StartingWindowController) obj;
                int i = StartingWindowController.IStartingWindowImpl.$r8$clinit;
                Objects.requireNonNull(iStartingWindowImpl);
                StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0 startingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0 = iStartingWindowImpl.mStartingWindowListener;
                Objects.requireNonNull(startingWindowController);
                startingWindowController.mTaskLaunchingCallback = startingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0;
                return;
        }
    }
}
