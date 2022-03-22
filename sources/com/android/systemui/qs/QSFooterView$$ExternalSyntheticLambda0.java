package com.android.systemui.qs;

import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import com.android.keyguard.KeyguardPatternView;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.biometrics.Utils;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.screenshot.ScreenshotEvent;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback;
import com.android.wm.shell.bubbles.BubbleExpandedView$1$$ExternalSyntheticLambda1;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QSFooterView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ QSFooterView$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        boolean z = true;
        switch (this.$r8$classId) {
            case 0:
                QSFooterView qSFooterView = (QSFooterView) this.f$0;
                int i2 = QSFooterView.$r8$clinit;
                Objects.requireNonNull(qSFooterView);
                TextView textView = qSFooterView.mBuildText;
                if (!qSFooterView.mExpanded || !qSFooterView.mShouldShowBuildText) {
                    i = 4;
                } else {
                    i = 0;
                }
                textView.setVisibility(i);
                TextView textView2 = qSFooterView.mBuildText;
                if (textView2.getVisibility() != 0) {
                    z = false;
                }
                textView2.setLongClickable(z);
                qSFooterView.setClickable(false);
                return;
            case 1:
                KeyguardPatternView keyguardPatternView = (KeyguardPatternView) this.f$0;
                int i3 = KeyguardPatternView.$r8$clinit;
                Objects.requireNonNull(keyguardPatternView);
                keyguardPatternView.enableClipping(true);
                return;
            case 2:
                AuthBiometricView authBiometricView = (AuthBiometricView) this.f$0;
                int i4 = AuthBiometricView.$r8$clinit;
                Objects.requireNonNull(authBiometricView);
                authBiometricView.updateState(authBiometricView.getStateForAfterError());
                authBiometricView.handleResetAfterError();
                Utils.notifyAccessibilityContentChanged(authBiometricView.mAccessibilityManager, authBiometricView);
                return;
            case 3:
                QSTileImpl qSTileImpl = (QSTileImpl) this.f$0;
                boolean z2 = QSTileImpl.DEBUG;
                Objects.requireNonNull(qSTileImpl);
                qSTileImpl.mLifecycle.setCurrentState(Lifecycle.State.DESTROYED);
                return;
            case 4:
                ScrollCaptureController scrollCaptureController = (ScrollCaptureController) this.f$0;
                Objects.requireNonNull(scrollCaptureController);
                try {
                    scrollCaptureController.mSession = (ScrollCaptureClient.Session) scrollCaptureController.mSessionFuture.get();
                    scrollCaptureController.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_STARTED, 0, scrollCaptureController.mWindowOwner);
                    scrollCaptureController.requestNextTile(0);
                    return;
                } catch (InterruptedException | ExecutionException e) {
                    Log.e("Screenshot", "session start failed!");
                    scrollCaptureController.mCaptureCompleter.setException(e);
                    scrollCaptureController.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_FAILURE, 0, scrollCaptureController.mWindowOwner);
                    return;
                }
            default:
                StatusBarRemoteInputCallback statusBarRemoteInputCallback = (StatusBarRemoteInputCallback) this.f$0;
                Objects.requireNonNull(statusBarRemoteInputCallback);
                View view = statusBarRemoteInputCallback.mPendingWorkRemoteInputView;
                if (view != null) {
                    ViewParent parent = view.getParent();
                    while (!(parent instanceof ExpandableNotificationRow)) {
                        if (parent != null) {
                            parent = parent.getParent();
                        } else {
                            return;
                        }
                    }
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) parent;
                    ViewParent parent2 = expandableNotificationRow.getParent();
                    if (parent2 instanceof NotificationStackScrollLayout) {
                        NotificationStackScrollLayout notificationStackScrollLayout = (NotificationStackScrollLayout) parent2;
                        expandableNotificationRow.setUserExpanded(true, true);
                        if (expandableNotificationRow.isChildInGroup()) {
                            expandableNotificationRow.mGroupExpansionManager.setGroupExpanded(expandableNotificationRow.mEntry, true);
                        }
                        expandableNotificationRow.notifyHeightChanged(false);
                        expandableNotificationRow.post(new BubbleExpandedView$1$$ExternalSyntheticLambda1(statusBarRemoteInputCallback, notificationStackScrollLayout, expandableNotificationRow, 1));
                        return;
                    }
                    return;
                }
                return;
        }
    }
}
