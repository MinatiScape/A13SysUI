package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ZenModeControllerImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ ZenModeControllerImpl$$ExternalSyntheticLambda1(boolean z) {
        this.f$0 = z;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        Objects.requireNonNull((ZenModeController.Callback) obj);
    }
}
