package com.android.systemui.biometrics;

import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                UdfpsController.UdfpsOverlayController udfpsOverlayController = (UdfpsController.UdfpsOverlayController) this.f$0;
                String str = (String) this.f$1;
                Objects.requireNonNull(udfpsOverlayController);
                UdfpsControllerOverlay udfpsControllerOverlay = UdfpsController.this.mOverlay;
                if (udfpsControllerOverlay != null) {
                    if (udfpsControllerOverlay.overlayView == null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z) {
                        Objects.requireNonNull(udfpsControllerOverlay);
                        UdfpsView udfpsView = udfpsControllerOverlay.overlayView;
                        Objects.requireNonNull(udfpsView);
                        udfpsView.debugMessage = str;
                        udfpsView.postInvalidate();
                        return;
                    }
                    return;
                }
                return;
            default:
                StackScrollerDecorView stackScrollerDecorView = (StackScrollerDecorView) this.f$0;
                int i = StackScrollerDecorView.$r8$clinit;
                Objects.requireNonNull(stackScrollerDecorView);
                stackScrollerDecorView.mContentVisibilityEndRunnable.run();
                ((Runnable) this.f$1).run();
                return;
        }
    }
}
