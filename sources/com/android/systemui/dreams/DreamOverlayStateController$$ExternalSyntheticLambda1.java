package com.android.systemui.dreams;

import com.android.wm.shell.compatui.CompatUIController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DreamOverlayStateController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ DreamOverlayStateController$$ExternalSyntheticLambda1(Object obj, boolean z, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                DreamOverlayStateController dreamOverlayStateController = (DreamOverlayStateController) this.f$0;
                boolean z = this.f$1;
                Objects.requireNonNull(dreamOverlayStateController);
                dreamOverlayStateController.mShouldShowComplications = z;
                dreamOverlayStateController.mCallbacks.forEach(DreamOverlayStateController$$ExternalSyntheticLambda7.INSTANCE);
                return;
            default:
                CompatUIController.CompatUIImpl compatUIImpl = (CompatUIController.CompatUIImpl) this.f$0;
                boolean z2 = this.f$1;
                Objects.requireNonNull(compatUIImpl);
                CompatUIController.this.onKeyguardOccludedChanged(z2);
                return;
        }
    }
}
