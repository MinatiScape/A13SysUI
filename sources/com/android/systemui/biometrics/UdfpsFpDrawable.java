package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Canvas;
/* compiled from: UdfpsFpDrawable.kt */
/* loaded from: classes.dex */
public final class UdfpsFpDrawable extends UdfpsDrawable {
    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        if (!this.isIlluminationShowing) {
            this.fingerprintDrawable.draw(canvas);
        }
    }

    public UdfpsFpDrawable(Context context) {
        super(context);
    }
}
