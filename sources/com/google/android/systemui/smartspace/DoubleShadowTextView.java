package com.google.android.systemui.smartspace;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
/* loaded from: classes.dex */
public class DoubleShadowTextView extends TextView {
    public final float mAmbientShadowBlur;
    public final int mAmbientShadowColor;
    public boolean mDrawShadow;
    public final float mKeyShadowBlur;
    public final int mKeyShadowColor;
    public final float mKeyShadowOffsetX;
    public final float mKeyShadowOffsetY;

    public DoubleShadowTextView(Context context) {
        this(context, null);
    }

    public DoubleShadowTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.widget.TextView, android.view.View
    public final void onDraw(Canvas canvas) {
        if (!this.mDrawShadow) {
            getPaint().clearShadowLayer();
            super.onDraw(canvas);
            return;
        }
        getPaint().setShadowLayer(this.mAmbientShadowBlur, 0.0f, 0.0f, this.mAmbientShadowColor);
        super.onDraw(canvas);
        canvas.save();
        canvas.clipRect(getScrollX(), getExtendedPaddingTop() + getScrollY(), getWidth() + getScrollX(), getHeight() + getScrollY());
        getPaint().setShadowLayer(this.mKeyShadowBlur, this.mKeyShadowOffsetX, this.mKeyShadowOffsetY, this.mKeyShadowColor);
        super.onDraw(canvas);
        canvas.restore();
    }

    public DoubleShadowTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDrawShadow = ColorUtils.calculateLuminance(getCurrentTextColor()) > 0.5d;
        this.mKeyShadowBlur = context.getResources().getDimensionPixelSize(2131165826);
        this.mKeyShadowOffsetX = context.getResources().getDimensionPixelSize(2131165824);
        this.mKeyShadowOffsetY = context.getResources().getDimensionPixelSize(2131165825);
        this.mKeyShadowColor = context.getResources().getColor(2131099887);
        this.mAmbientShadowBlur = context.getResources().getDimensionPixelSize(2131165326);
        this.mAmbientShadowColor = context.getResources().getColor(2131099696);
    }

    @Override // android.widget.TextView
    public final void setTextColor(int i) {
        boolean z;
        super.setTextColor(i);
        if (ColorUtils.calculateLuminance(i) > 0.5d) {
            z = true;
        } else {
            z = false;
        }
        this.mDrawShadow = z;
    }
}
