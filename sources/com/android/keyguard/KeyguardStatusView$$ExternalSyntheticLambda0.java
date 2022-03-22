package com.android.keyguard;

import android.app.ActivityManager;
import android.util.Log;
import com.android.keyguard.LockIconViewController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.dialog.InternetDialog;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.AlarmTimeout;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.animation.StackAnimationController;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.pip.phone.PipMenuView;
import com.google.android.systemui.communal.dock.callbacks.TimeoutToUserZeroCallback;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardStatusView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ KeyguardStatusView$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                KeyguardStatusView keyguardStatusView = (KeyguardStatusView) this.f$0;
                int i = KeyguardStatusView.$r8$clinit;
                Objects.requireNonNull(keyguardStatusView);
                KeyguardSliceView keyguardSliceView = keyguardStatusView.mKeyguardSlice;
                Objects.requireNonNull(keyguardSliceView);
                boolean z = keyguardSliceView.mHasHeader;
                if (keyguardStatusView.mShowingHeader != z) {
                    keyguardStatusView.mShowingHeader = z;
                    return;
                }
                return;
            case 1:
                LockIconViewController.AnonymousClass3 r5 = (LockIconViewController.AnonymousClass3) this.f$0;
                Objects.requireNonNull(r5);
                LockIconViewController.this.updateVisibility();
                return;
            case 2:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                boolean z2 = InternetDialog.DEBUG;
                Objects.requireNonNull(internetDialog);
                internetDialog.updateDialog(true);
                return;
            case 3:
                AutoTileManager.AnonymousClass4 r52 = (AutoTileManager.AnonymousClass4) this.f$0;
                Objects.requireNonNull(r52);
                AutoTileManager.this.mDeviceControlsController.removeCallback();
                return;
            case 4:
                ((AlarmTimeout) this.f$0).cancel();
                return;
            case 5:
                StatusBarKeyguardViewManager statusBarKeyguardViewManager = (StatusBarKeyguardViewManager) this.f$0;
                Objects.requireNonNull(statusBarKeyguardViewManager);
                statusBarKeyguardViewManager.mViewMediatorCallback.readyForKeyguardDone();
                return;
            case FalsingManager.VERSION /* 6 */:
                Clock.AnonymousClass2 r53 = (Clock.AnonymousClass2) this.f$0;
                int i2 = Clock.AnonymousClass2.$r8$clinit;
                Objects.requireNonNull(r53);
                Clock.this.updateClock();
                return;
            case 7:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i3 = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                if (!bubbleStackView.mTemporarilyInvisible || bubbleStackView.mFlyout.getVisibility() == 0) {
                    bubbleStackView.animate().translationX(0.0f).start();
                    return;
                } else if (bubbleStackView.mStackAnimationController.isStackOnLeftSide()) {
                    bubbleStackView.animate().translationX(-bubbleStackView.mBubbleSize).start();
                    return;
                } else {
                    bubbleStackView.animate().translationX(bubbleStackView.mBubbleSize).start();
                    return;
                }
            case 8:
                StackAnimationController stackAnimationController = (StackAnimationController) this.f$0;
                Objects.requireNonNull(stackAnimationController);
                stackAnimationController.setStackPosition(stackAnimationController.mPositioner.getRestingPosition());
                stackAnimationController.mStackMovedToStartPosition = true;
                stackAnimationController.mLayout.setVisibility(0);
                if (stackAnimationController.mLayout.getChildCount() > 0) {
                    FloatingContentCoordinator floatingContentCoordinator = stackAnimationController.mFloatingContentCoordinator;
                    StackAnimationController.AnonymousClass1 r2 = stackAnimationController.mStackFloatingContent;
                    Objects.requireNonNull(floatingContentCoordinator);
                    floatingContentCoordinator.updateContentBounds();
                    floatingContentCoordinator.allContentBounds.put(r2, r2.getFloatingBoundsOnScreen());
                    floatingContentCoordinator.maybeMoveConflictingContent(r2);
                    stackAnimationController.animateInBubble(stackAnimationController.mLayout.getChildAt(0), 0);
                    return;
                }
                return;
            case 9:
                PipMenuView pipMenuView = (PipMenuView) this.f$0;
                Objects.requireNonNull(pipMenuView);
                pipMenuView.hideMenu(null, true, pipMenuView.mDidLastShowMenuResize, 1);
                return;
            default:
                TimeoutToUserZeroCallback timeoutToUserZeroCallback = (TimeoutToUserZeroCallback) this.f$0;
                boolean z3 = TimeoutToUserZeroCallback.DEBUG;
                Objects.requireNonNull(timeoutToUserZeroCallback);
                timeoutToUserZeroCallback.mCancelTimerRunnable = null;
                if (TimeoutToUserZeroCallback.DEBUG) {
                    Log.d("TimeoutToUserZero", "switching to user 0");
                }
                UserSwitcherController userSwitcherController = timeoutToUserZeroCallback.mUserSwitcherController;
                Objects.requireNonNull(userSwitcherController);
                if (userSwitcherController.mUserTracker.getUserId() != 0) {
                    if (!userSwitcherController.mPauseRefreshUsers) {
                        userSwitcherController.mHandler.postDelayed(userSwitcherController.mUnpauseRefreshUsers, 3000L);
                        userSwitcherController.mPauseRefreshUsers = true;
                    }
                    ActivityManager.logoutCurrentUser();
                    return;
                }
                return;
        }
    }
}
