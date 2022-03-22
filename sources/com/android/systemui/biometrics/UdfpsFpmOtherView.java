package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UdfpsFpmOtherView.kt */
/* loaded from: classes.dex */
public final class UdfpsFpmOtherView extends UdfpsAnimationView {
    public final UdfpsFpDrawable fingerprintDrawable;

    public UdfpsFpmOtherView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.fingerprintDrawable = new UdfpsFpDrawable(context);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        View findViewById = findViewById(2131429141);
        Intrinsics.checkNotNull(findViewById);
        ((ImageView) findViewById).setImageDrawable(this.fingerprintDrawable);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public final UdfpsDrawable getDrawable() {
        return this.fingerprintDrawable;
    }
}
