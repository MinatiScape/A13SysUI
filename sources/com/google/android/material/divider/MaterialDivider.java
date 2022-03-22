package com.google.android.material.divider;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class MaterialDivider extends View {
    public int color;
    public final MaterialShapeDrawable dividerDrawable;
    public int insetEnd;
    public int insetStart;
    public int thickness;

    public MaterialDivider(Context context, AttributeSet attributeSet) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, 2130969439, 2132018689), attributeSet, 2130969439);
        Context context2 = getContext();
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        this.dividerDrawable = materialShapeDrawable;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.MaterialDivider, 2130969439, 2132018689, new int[0]);
        this.thickness = obtainStyledAttributes.getDimensionPixelSize(3, getResources().getDimensionPixelSize(2131166311));
        this.insetStart = obtainStyledAttributes.getDimensionPixelOffset(2, 0);
        this.insetEnd = obtainStyledAttributes.getDimensionPixelOffset(1, 0);
        int defaultColor = MaterialResources.getColorStateList(context2, obtainStyledAttributes, 0).getDefaultColor();
        if (this.color != defaultColor) {
            this.color = defaultColor;
            materialShapeDrawable.setFillColor(ColorStateList.valueOf(defaultColor));
            invalidate();
        }
        obtainStyledAttributes.recycle();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        super.onDraw(canvas);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        boolean z = true;
        if (ViewCompat.Api17Impl.getLayoutDirection(this) != 1) {
            z = false;
        }
        if (z) {
            i = this.insetEnd;
        } else {
            i = this.insetStart;
        }
        if (z) {
            i3 = getWidth();
            i2 = this.insetStart;
        } else {
            i3 = getWidth();
            i2 = this.insetEnd;
        }
        this.dividerDrawable.setBounds(i, 0, i3 - i2, getBottom() - getTop());
        this.dividerDrawable.draw(canvas);
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int mode = View.MeasureSpec.getMode(i2);
        int measuredHeight = getMeasuredHeight();
        if (mode == Integer.MIN_VALUE || mode == 0) {
            int i3 = this.thickness;
            if (i3 > 0 && measuredHeight != i3) {
                measuredHeight = i3;
            }
            setMeasuredDimension(getMeasuredWidth(), measuredHeight);
        }
    }
}
