package com.android.systemui.scrim;

import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScrimView$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ ScrimView f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2 = false;

    public /* synthetic */ ScrimView$$ExternalSyntheticLambda4(ScrimView scrimView, int i) {
        this.f$0 = scrimView;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ScrimView scrimView = this.f$0;
        int i = this.f$1;
        boolean z = this.f$2;
        int i2 = ScrimView.$r8$clinit;
        Objects.requireNonNull(scrimView);
        if (scrimView.mTintColor != i) {
            scrimView.mTintColor = i;
            scrimView.updateColorWithTint(z);
        }
    }
}
