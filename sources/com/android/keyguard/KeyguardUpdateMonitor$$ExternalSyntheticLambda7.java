package com.android.keyguard;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.Choreographer;
import com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.condition.Monitor;
import com.android.wm.shell.bubbles.animation.ExpandedAnimationController;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.phone.PipController;
import com.android.wm.shell.startingsurface.StartingSurfaceDrawer;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardUpdateMonitor$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ KeyguardUpdateMonitor$$ExternalSyntheticLambda7(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        switch (this.$r8$classId) {
            case 0:
                KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) this.f$0;
                Objects.requireNonNull(keyguardUpdateMonitor);
                keyguardUpdateMonitor.updateFaceListeningState(2);
                return;
            case 1:
                KeyguardIndicationRotateTextViewController.ShowNextIndication showNextIndication = (KeyguardIndicationRotateTextViewController.ShowNextIndication) this.f$0;
                Objects.requireNonNull(showNextIndication);
                if (KeyguardIndicationRotateTextViewController.this.mIndicationQueue.size() == 0) {
                    i = -1;
                } else {
                    i = ((Integer) KeyguardIndicationRotateTextViewController.this.mIndicationQueue.get(0)).intValue();
                }
                KeyguardIndicationRotateTextViewController.this.showIndication(i);
                return;
            case 2:
                PhoneStatusBarPolicy phoneStatusBarPolicy = (PhoneStatusBarPolicy) this.f$0;
                boolean z = PhoneStatusBarPolicy.DEBUG;
                Objects.requireNonNull(phoneStatusBarPolicy);
                phoneStatusBarPolicy.mIconController.setIconVisibility(phoneStatusBarPolicy.mSlotScreenRecord, false);
                return;
            case 3:
                UserSwitcherController userSwitcherController = (UserSwitcherController) this.f$0;
                Objects.requireNonNull(userSwitcherController);
                int createGuest = userSwitcherController.createGuest();
                userSwitcherController.mGuestCreationScheduled.set(false);
                userSwitcherController.mGuestIsResetting.set(false);
                if (createGuest == -10000) {
                    Log.w("UserSwitcherController", "Could not create new guest while exiting existing guest");
                    userSwitcherController.refreshUsers(-10000);
                    return;
                }
                return;
            case 4:
                Monitor.AnonymousClass1 r3 = (Monitor.AnonymousClass1) this.f$0;
                Objects.requireNonNull(r3);
                Monitor.this.updateConditionMetState();
                return;
            case 5:
                ExpandedAnimationController expandedAnimationController = (ExpandedAnimationController) this.f$0;
                Objects.requireNonNull(expandedAnimationController);
                expandedAnimationController.mLeadBubbleEndAction = null;
                return;
            case FalsingManager.VERSION /* 6 */:
                ((ValueAnimator) this.f$0).start();
                return;
            case 7:
                PipController.PipImpl pipImpl = (PipController.PipImpl) this.f$0;
                Objects.requireNonNull(pipImpl);
                PipController pipController = PipController.this;
                Objects.requireNonNull(pipController);
                PipTaskOrganizer pipTaskOrganizer = pipController.mPipTaskOrganizer;
                Context context = pipController.mContext;
                Objects.requireNonNull(pipTaskOrganizer);
                PipSurfaceTransactionHelper pipSurfaceTransactionHelper = pipTaskOrganizer.mSurfaceTransactionHelper;
                Objects.requireNonNull(pipSurfaceTransactionHelper);
                pipSurfaceTransactionHelper.mCornerRadius = context.getResources().getDimensionPixelSize(2131166780);
                pipController.onPipCornerRadiusChanged();
                return;
            default:
                StartingSurfaceDrawer startingSurfaceDrawer = (StartingSurfaceDrawer) this.f$0;
                Objects.requireNonNull(startingSurfaceDrawer);
                startingSurfaceDrawer.mChoreographer = Choreographer.getInstance();
                return;
        }
    }
}
