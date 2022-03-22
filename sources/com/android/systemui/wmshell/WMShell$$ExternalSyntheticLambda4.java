package com.android.systemui.wmshell;

import android.view.GestureDetector;
import android.view.MotionEvent;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.onehanded.OneHandedAnimationCallback;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WMShell$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WMShell$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((WMShell) this.f$0).initHideDisplayCutout((HideDisplayCutout) obj);
                return;
            case 1:
                ((GestureDetector.OnGestureListener) obj).onShowPress((MotionEvent) this.f$0);
                return;
            default:
                OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator = (OneHandedAnimationController.OneHandedTransitionAnimator) this.f$0;
                int i = OneHandedAnimationController.OneHandedTransitionAnimator.$r8$clinit;
                Objects.requireNonNull(oneHandedTransitionAnimator);
                ((OneHandedAnimationCallback) obj).onOneHandedAnimationStart(oneHandedTransitionAnimator);
                return;
        }
    }
}
