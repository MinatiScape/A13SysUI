package com.android.wm.shell.pip;

import android.animation.AnimatorSet;
import com.android.systemui.media.dialog.MediaOutputBaseDialog;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.connectivity.WifiSignalController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.util.Assert;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.phone.PipMenuView;
import com.google.android.systemui.elmyra.actions.Action;
import com.google.android.systemui.smartspace.InterceptingViewPager;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                PipAnimationController.PipTransitionAnimator pipTransitionAnimator = (PipAnimationController.PipTransitionAnimator) this.f$0;
                Objects.requireNonNull(pipTransitionAnimator);
                pipTransitionAnimator.mContentOverlay = null;
                return;
            case 1:
                MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) this.f$0;
                int i = MediaOutputBaseDialog.$r8$clinit;
                Objects.requireNonNull(mediaOutputBaseDialog);
                mediaOutputBaseDialog.refresh(true);
                return;
            case 2:
                WifiSignalController wifiSignalController = (WifiSignalController) this.f$0;
                int i2 = WifiSignalController.$r8$clinit;
                Objects.requireNonNull(wifiSignalController);
                Assert.isMainThread();
                wifiSignalController.copyWifiStates();
                wifiSignalController.notifyListenersIfNecessary();
                return;
            case 3:
                AutoTileManager.AutoAddSetting autoAddSetting = (AutoTileManager.AutoAddSetting) this.f$0;
                int i3 = AutoTileManager.AutoAddSetting.$r8$clinit;
                Objects.requireNonNull(autoAddSetting);
                autoAddSetting.setListening(false);
                return;
            case 4:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i4 = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.mBubbleContainer.setActiveController(bubbleStackView.mStackAnimationController);
                return;
            case 5:
                PipMenuView pipMenuView = (PipMenuView) this.f$0;
                int i5 = PipMenuView.$r8$clinit;
                Objects.requireNonNull(pipMenuView);
                AnimatorSet animatorSet = pipMenuView.mMenuContainerAnimator;
                if (animatorSet != null) {
                    animatorSet.setStartDelay(30L);
                    pipMenuView.setVisibility(0);
                    pipMenuView.mMenuContainerAnimator.start();
                    return;
                }
                return;
            case FalsingManager.VERSION /* 6 */:
                Action action = (Action) this.f$0;
                Objects.requireNonNull(action);
                action.updateFeedbackEffects(0.0f, 0);
                return;
            default:
                InterceptingViewPager interceptingViewPager = (InterceptingViewPager) this.f$0;
                int i6 = InterceptingViewPager.$r8$clinit;
                Objects.requireNonNull(interceptingViewPager);
                interceptingViewPager.mHasPerformedLongPress = true;
                if (interceptingViewPager.performLongClick()) {
                    interceptingViewPager.getParent().requestDisallowInterceptTouchEvent(true);
                    return;
                }
                return;
        }
    }
}
