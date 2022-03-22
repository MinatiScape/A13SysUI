package com.android.systemui.dreams;

import android.os.CancellationSignal;
import com.android.systemui.dreams.DreamOverlayStateController;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DreamOverlayStateController$$ExternalSyntheticLambda8 implements Consumer {
    public static final /* synthetic */ DreamOverlayStateController$$ExternalSyntheticLambda8 INSTANCE = new DreamOverlayStateController$$ExternalSyntheticLambda8(0);
    public static final /* synthetic */ DreamOverlayStateController$$ExternalSyntheticLambda8 INSTANCE$1 = new DreamOverlayStateController$$ExternalSyntheticLambda8(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ DreamOverlayStateController$$ExternalSyntheticLambda8(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                int i = DreamOverlayStateController.$r8$clinit;
                ((DreamOverlayStateController.Callback) obj).onStateChanged();
                return;
            default:
                ((CancellationSignal) obj).cancel();
                return;
        }
    }
}
