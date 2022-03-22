package com.android.systemui.scrim;

import android.graphics.drawable.ColorDrawable;
import android.metrics.LogMaker;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView;
import com.android.systemui.assist.AssistantSessionEvent;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleBadgeIconFactory;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.BubbleIconFactory;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda28;
import com.android.wm.shell.bubbles.animation.ExpandedAnimationController;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.phone.PipController;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.google.android.systemui.assist.uihints.NgaUiController;
import com.google.android.systemui.dreamliner.DockIndicationController;
import java.util.List;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScrimView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ScrimView$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ScrimView scrimView = (ScrimView) this.f$0;
                int i = ScrimView.$r8$clinit;
                Objects.requireNonNull(scrimView);
                scrimView.updateColorWithTint(false);
                return;
            case 1:
                CarrierTextManager carrierTextManager = (CarrierTextManager) this.f$0;
                boolean z = CarrierTextManager.DEBUG;
                Objects.requireNonNull(carrierTextManager);
                carrierTextManager.mKeyguardUpdateMonitor.removeCallback(carrierTextManager.mCallback);
                WakefulnessLifecycle wakefulnessLifecycle = carrierTextManager.mWakefulnessLifecycle;
                CarrierTextManager.AnonymousClass1 r9 = carrierTextManager.mWakefulnessObserver;
                Objects.requireNonNull(wakefulnessLifecycle);
                wakefulnessLifecycle.mObservers.remove(r9);
                return;
            case 2:
                KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) this.f$0;
                boolean z2 = KeyguardUpdateMonitor.DEBUG;
                Objects.requireNonNull(keyguardUpdateMonitor);
                keyguardUpdateMonitor.updateFingerprintListeningState(2);
                return;
            case 3:
                AccessibilityFloatingMenuView accessibilityFloatingMenuView = (AccessibilityFloatingMenuView) this.f$0;
                int i2 = AccessibilityFloatingMenuView.$r8$clinit;
                Objects.requireNonNull(accessibilityFloatingMenuView);
                accessibilityFloatingMenuView.mFadeOutAnimator.start();
                return;
            case 4:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                Objects.requireNonNull(bubblesImpl);
                BubbleController bubbleController = BubbleController.this;
                Objects.requireNonNull(bubbleController);
                BubbleStackView bubbleStackView = bubbleController.mStackView;
                if (bubbleStackView != null) {
                    bubbleStackView.setUpFlyout();
                    bubbleStackView.setUpManageMenu();
                    bubbleStackView.setUpDismissView();
                    bubbleStackView.updateOverflow();
                    bubbleStackView.updateUserEdu();
                    List<Bubble> bubbles = bubbleStackView.mBubbleData.getBubbles();
                    if (!bubbles.isEmpty()) {
                        bubbles.forEach(BubbleStackView$$ExternalSyntheticLambda28.INSTANCE);
                    }
                    bubbleStackView.mScrim.setBackgroundDrawable(new ColorDrawable(bubbleStackView.getResources().getColor(17170473)));
                    bubbleStackView.mManageMenuScrim.setBackgroundDrawable(new ColorDrawable(bubbleStackView.getResources().getColor(17170473)));
                }
                bubbleController.mBubbleIconFactory = new BubbleIconFactory(bubbleController.mContext);
                bubbleController.mBubbleBadgeIconFactory = new BubbleBadgeIconFactory(bubbleController.mContext);
                for (Bubble bubble : bubbleController.mBubbleData.getBubbles()) {
                    bubble.inflate(null, bubbleController.mContext, bubbleController, bubbleController.mStackView, bubbleController.mBubbleIconFactory, bubbleController.mBubbleBadgeIconFactory, false);
                }
                for (Bubble bubble2 : bubbleController.mBubbleData.getOverflowBubbles()) {
                    bubble2.inflate(null, bubbleController.mContext, bubbleController, bubbleController.mStackView, bubbleController.mBubbleIconFactory, bubbleController.mBubbleBadgeIconFactory, false);
                }
                return;
            case 5:
                ExpandedAnimationController expandedAnimationController = (ExpandedAnimationController) this.f$0;
                Objects.requireNonNull(expandedAnimationController);
                expandedAnimationController.mAnimatingExpand = false;
                Runnable runnable = expandedAnimationController.mAfterExpand;
                if (runnable != null) {
                    runnable.run();
                }
                expandedAnimationController.mAfterExpand = null;
                expandedAnimationController.updateBubblePositions();
                return;
            case FalsingManager.VERSION /* 6 */:
                PipController.PipImpl pipImpl = (PipController.PipImpl) this.f$0;
                Objects.requireNonNull(pipImpl);
                PipController pipController = PipController.this;
                Objects.requireNonNull(pipController);
                PipMediaController pipMediaController = pipController.mMediaController;
                Objects.requireNonNull(pipMediaController);
                pipMediaController.mMediaSessionManager.removeOnActiveSessionsChangedListener(pipMediaController.mSessionsChangedListener);
                pipMediaController.mMediaSessionManager.addOnActiveSessionsChangedListener(null, UserHandle.CURRENT, pipMediaController.mHandlerExecutor, pipMediaController.mSessionsChangedListener);
                return;
            case 7:
                ((PipTouchHandler) this.f$0).updateMovementBounds();
                return;
            case 8:
                NgaUiController ngaUiController = (NgaUiController) this.f$0;
                boolean z3 = NgaUiController.VERBOSE;
                Objects.requireNonNull(ngaUiController);
                if (ngaUiController.mShowingAssistUi) {
                    Log.e("NgaUiController", "Timed out");
                    ngaUiController.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_TIMEOUT_DISMISS);
                    ngaUiController.closeNgaUi();
                    MetricsLogger.action(new LogMaker(1716).setType(5).setSubtype(4));
                    return;
                }
                return;
            default:
                DockIndicationController dockIndicationController = (DockIndicationController) this.f$0;
                String str = DockIndicationController.ACTION_ASSISTANT_POODLE;
                Objects.requireNonNull(dockIndicationController);
                if (dockIndicationController.mDozing && dockIndicationController.mDocking) {
                    dockIndicationController.mDockPromo.startAnimation(dockIndicationController.mHidePromoAnimation);
                    return;
                }
                return;
        }
    }
}
