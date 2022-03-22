package com.android.systemui.dreams;

import com.android.systemui.dreams.complication.Complication;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DreamOverlayStateController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ DreamOverlayStateController$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                DreamOverlayStateController dreamOverlayStateController = (DreamOverlayStateController) this.f$0;
                Objects.requireNonNull(dreamOverlayStateController);
                if (dreamOverlayStateController.mComplications.remove((Complication) this.f$1)) {
                    dreamOverlayStateController.mCallbacks.stream().forEach(DreamOverlayStateController$$ExternalSyntheticLambda6.INSTANCE);
                    return;
                }
                return;
            default:
                HideDisplayCutoutController.HideDisplayCutoutImpl hideDisplayCutoutImpl = (HideDisplayCutoutController.HideDisplayCutoutImpl) this.f$0;
                Objects.requireNonNull(hideDisplayCutoutImpl);
                HideDisplayCutoutController hideDisplayCutoutController = HideDisplayCutoutController.this;
                Objects.requireNonNull(hideDisplayCutoutController);
                hideDisplayCutoutController.updateStatus();
                return;
        }
    }
}
