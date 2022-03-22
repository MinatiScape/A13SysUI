package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
/* loaded from: classes.dex */
public class NumPadButton extends AlphaOptimizedImageButton {
    public NumPadAnimator mAnimator;
    public int mOrientation;

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        this.mOrientation = configuration.orientation;
    }

    public final void reloadColors() {
        NumPadAnimator numPadAnimator = this.mAnimator;
        if (numPadAnimator != null) {
            numPadAnimator.reloadColors(getContext());
        }
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16842809});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        ((VectorDrawable) getDrawable()).setTintList(ColorStateList.valueOf(color));
    }

    public NumPadButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (getBackground() instanceof RippleDrawable) {
            this.mAnimator = new NumPadAnimator(context, (RippleDrawable) getBackground(), attributeSet.getStyleAttribute());
        } else {
            this.mAnimator = null;
        }
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        NumPadAnimator numPadAnimator = this.mAnimator;
        if (numPadAnimator != null) {
            numPadAnimator.onLayout(i4 - i2);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onMeasure(int i, int i2) {
        boolean z;
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        if (this.mAnimator == null || this.mOrientation == 2) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            measuredWidth = (int) (measuredWidth * 0.66f);
        }
        setMeasuredDimension(getMeasuredWidth(), measuredWidth);
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        NumPadAnimator numPadAnimator;
        if (motionEvent.getActionMasked() == 0 && (numPadAnimator = this.mAnimator) != null) {
            numPadAnimator.mAnimator.cancel();
            numPadAnimator.mAnimator.start();
        }
        return super.onTouchEvent(motionEvent);
    }
}
