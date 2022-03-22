package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
/* loaded from: classes.dex */
public final class CircularProgressIndicatorSpec extends BaseProgressIndicatorSpec {
    public int indicatorDirection;
    public int indicatorInset;
    public int indicatorSize;

    @Override // com.google.android.material.progressindicator.BaseProgressIndicatorSpec
    public final void validateSpec() {
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CircularProgressIndicatorSpec(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 2130968786, 2132018652);
        int i = CircularProgressIndicator.$r8$clinit;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(2131166537);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(2131166532);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R$styleable.CircularProgressIndicator, 2130968786, 2132018652, new int[0]);
        this.indicatorSize = Math.max(MaterialResources.getDimensionPixelSize(context, obtainStyledAttributes, 2, dimensionPixelSize), this.trackThickness * 2);
        this.indicatorInset = MaterialResources.getDimensionPixelSize(context, obtainStyledAttributes, 1, dimensionPixelSize2);
        this.indicatorDirection = obtainStyledAttributes.getInt(0, 0);
        obtainStyledAttributes.recycle();
    }
}
