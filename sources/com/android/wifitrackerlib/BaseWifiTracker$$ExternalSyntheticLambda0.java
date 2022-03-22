package com.android.wifitrackerlib;

import android.animation.ValueAnimator;
import android.os.Vibrator;
import android.util.Log;
import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.wifitrackerlib.BaseWifiTracker;
import com.android.wm.shell.legacysplitscreen.DividerImeController;
import com.android.wm.shell.onehanded.OneHandedController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BaseWifiTracker$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BaseWifiTracker$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                BaseWifiTracker.Scanner scanner = (BaseWifiTracker.Scanner) this.f$0;
                int i = BaseWifiTracker.Scanner.$r8$clinit;
                Objects.requireNonNull(scanner);
                scanner.mIsActive = false;
                if (BaseWifiTracker.sVerboseLogging) {
                    Log.v(BaseWifiTracker.this.mTag, "Scanner stop");
                }
                scanner.mRetry = 0;
                scanner.removeCallbacksAndMessages(null);
                return;
            case 1:
                MagnificationModeSwitch magnificationModeSwitch = (MagnificationModeSwitch) this.f$0;
                Objects.requireNonNull(magnificationModeSwitch);
                magnificationModeSwitch.mImageView.animate().alpha(0.0f).setDuration(300L).withEndAction(new BaseWifiTracker$$ExternalSyntheticLambda1(magnificationModeSwitch, 1)).start();
                magnificationModeSwitch.mIsFadeOutAnimating = true;
                return;
            case 2:
                ((ClipboardOverlayController) this.f$0).animateOut();
                return;
            case 3:
                OverviewProxyService.AnonymousClass1 r10 = (OverviewProxyService.AnonymousClass1) this.f$0;
                int i2 = OverviewProxyService.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r10);
                r10.sendEvent(0);
                r10.sendEvent(1);
                OverviewProxyService.this.notifyBackAction(true, -1, -1, true, false);
                return;
            case 4:
                ((Vibrator) this.f$0).cancel();
                return;
            case 5:
                DividerImeController dividerImeController = (DividerImeController) this.f$0;
                Objects.requireNonNull(dividerImeController);
                if (!dividerImeController.mPaused) {
                    dividerImeController.mPaused = true;
                    dividerImeController.mPausedTargetAdjusted = dividerImeController.mTargetAdjusted;
                    dividerImeController.mTargetAdjusted = false;
                    dividerImeController.mTargetSecondaryDim = 0.0f;
                    dividerImeController.mTargetPrimaryDim = 0.0f;
                    dividerImeController.updateImeAdjustState(false);
                    dividerImeController.startAsyncAnimation();
                    ValueAnimator valueAnimator = dividerImeController.mAnimation;
                    if (valueAnimator != null) {
                        valueAnimator.end();
                        return;
                    }
                    return;
                }
                return;
            default:
                ((OneHandedController) this.f$0).onEnabledSettingChanged();
                return;
        }
    }
}
