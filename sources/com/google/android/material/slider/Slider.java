package com.google.android.material.slider;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
/* loaded from: classes.dex */
public class Slider extends BaseSlider<Slider, Object, Object> {
    public Slider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{16842788});
        if (obtainStyledAttributes.hasValue(0)) {
            setValues(Float.valueOf(obtainStyledAttributes.getFloat(0, 0.0f)));
        }
        obtainStyledAttributes.recycle();
    }

    @Override // com.google.android.material.slider.BaseSlider
    public final boolean pickActiveThumb() {
        if (this.activeThumbIdx != -1) {
            return true;
        }
        this.activeThumbIdx = 0;
        return true;
    }
}
