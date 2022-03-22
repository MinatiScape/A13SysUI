package com.android.systemui.dreams;

import com.android.systemui.dreams.DreamOverlayStateController;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DreamOverlayStateController$$ExternalSyntheticLambda6 implements Consumer {
    public static final /* synthetic */ DreamOverlayStateController$$ExternalSyntheticLambda6 INSTANCE = new DreamOverlayStateController$$ExternalSyntheticLambda6();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        int i = DreamOverlayStateController.$r8$clinit;
        ((DreamOverlayStateController.Callback) obj).onComplicationsChanged();
    }
}
