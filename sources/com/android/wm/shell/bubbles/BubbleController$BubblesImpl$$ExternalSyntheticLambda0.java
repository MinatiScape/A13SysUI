package com.android.wm.shell.bubbles;

import android.telephony.PinResult;
import com.android.keyguard.KeyguardSimPinViewController;
import com.android.wm.shell.bubbles.BubbleController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                BubbleEntry bubbleEntry = (BubbleEntry) this.f$1;
                Objects.requireNonNull(bubblesImpl);
                BubbleController bubbleController = BubbleController.this;
                Objects.requireNonNull(bubbleController);
                if (BubbleController.canLaunchInTaskView(bubbleController.mContext, bubbleEntry)) {
                    bubbleController.updateBubble(bubbleEntry);
                    return;
                }
                return;
            default:
                KeyguardSimPinViewController.CheckSimPin checkSimPin = (KeyguardSimPinViewController.CheckSimPin) this.f$0;
                int i = KeyguardSimPinViewController.CheckSimPin.$r8$clinit;
                Objects.requireNonNull(checkSimPin);
                checkSimPin.onSimCheckResponse((PinResult) this.f$1);
                return;
        }
    }
}
