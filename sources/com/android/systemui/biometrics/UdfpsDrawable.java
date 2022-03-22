package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
/* compiled from: UdfpsDrawable.kt */
/* loaded from: classes.dex */
public abstract class UdfpsDrawable extends Drawable {
    public int _alpha = 255;
    public final ShapeDrawable fingerprintDrawable;
    public boolean isIlluminationShowing;

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
    }

    public UdfpsDrawable(Context context) {
        ShapeDrawable shapeDrawable = (ShapeDrawable) ((UdfpsDrawableKt$defaultFactory$1) UdfpsDrawableKt.defaultFactory).invoke(context);
        this.fingerprintDrawable = shapeDrawable;
        shapeDrawable.getPaint().getStrokeWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this._alpha = i;
        this.fingerprintDrawable.setAlpha(i);
        invalidateSelf();
    }

    public void updateFingerprintIconBounds(Rect rect) {
        this.fingerprintDrawable.setBounds(rect);
        invalidateSelf();
    }

    public void onSensorRectUpdated(RectF rectF) {
        int height = ((int) rectF.height()) / 8;
        updateFingerprintIconBounds(new Rect(((int) rectF.left) + height, ((int) rectF.top) + height, ((int) rectF.right) - height, ((int) rectF.bottom) - height));
    }

    @Override // android.graphics.drawable.Drawable
    public final int getAlpha() {
        return this._alpha;
    }
}
