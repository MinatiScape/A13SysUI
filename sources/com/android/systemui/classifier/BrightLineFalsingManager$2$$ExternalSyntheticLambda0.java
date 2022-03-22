package com.android.systemui.classifier;

import android.view.SurfaceControl;
import com.android.systemui.plugins.FalsingManager;
import com.android.wm.shell.onehanded.OneHandedController;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BrightLineFalsingManager$2$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ BrightLineFalsingManager$2$$ExternalSyntheticLambda0 INSTANCE = new BrightLineFalsingManager$2$$ExternalSyntheticLambda0(0);
    public static final /* synthetic */ BrightLineFalsingManager$2$$ExternalSyntheticLambda0 INSTANCE$1 = new BrightLineFalsingManager$2$$ExternalSyntheticLambda0(1);
    public static final /* synthetic */ BrightLineFalsingManager$2$$ExternalSyntheticLambda0 INSTANCE$2 = new BrightLineFalsingManager$2$$ExternalSyntheticLambda0(2);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ BrightLineFalsingManager$2$$ExternalSyntheticLambda0(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((FalsingManager.FalsingBeliefListener) obj).onFalse();
                return;
            case 1:
                ((SurfaceControl) obj).release();
                return;
            default:
                int i = OneHandedController.IOneHandedImpl.$r8$clinit;
                ((OneHandedController) obj).startOneHanded();
                return;
        }
    }
}
