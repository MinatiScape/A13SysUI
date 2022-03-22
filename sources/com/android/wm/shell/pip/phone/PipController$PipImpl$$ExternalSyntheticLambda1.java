package com.android.wm.shell.pip.phone;

import android.os.SystemClock;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.phone.PipController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda1(Object obj, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = obj;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                PipController.PipImpl pipImpl = (PipController.PipImpl) this.f$0;
                int i = this.f$1;
                Objects.requireNonNull(pipImpl);
                PipController pipController = PipController.this;
                Objects.requireNonNull(pipController);
                PipTaskOrganizer pipTaskOrganizer = pipController.mPipTaskOrganizer;
                Objects.requireNonNull(pipTaskOrganizer);
                pipTaskOrganizer.mOneShotAnimationType = i;
                boolean z = true;
                if (i == 1) {
                    pipTaskOrganizer.mLastOneShotAlphaAnimationTime = SystemClock.uptimeMillis();
                }
                PipTransitionController pipTransitionController = pipController.mPipTransitionController;
                if (i != 0) {
                    z = false;
                }
                pipTransitionController.setIsFullAnimation(z);
                return;
            default:
                BubbleController.BubblesImpl bubblesImpl = (BubbleController.BubblesImpl) this.f$0;
                int i2 = this.f$1;
                Objects.requireNonNull(bubblesImpl);
                BubbleController.this.onUserChanged(i2);
                return;
        }
    }
}
